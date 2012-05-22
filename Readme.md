URI Tunneling    (Draft)
=============

URI Tunneling, es la opción mas básica de integración sobre [HTTP][2], permite transferir información a través de
los límites del sistema codificando la información en la URI misma. Web API´s que usan este estilo son 
categorizadas en el nivel 1 del modelo de [Richardson][1]. URI Tunneling, es una técnica simple y fácil de 
implementar, no se necesita de ningún framework especial para poder implementarlo. 
En Java, por ejemplo podemos utilizar solamente Servlets.

Un cliente que requiera hacer interacciones con una API de este estilo requiere conocer
* El template de las URIs
* El formato de la respuesta
* El comportamiento esperado en cada interacción.

Generalmente se utilizan los métodos _GET_ y _POST_ según corresponda. _GET_ se utiliza cuando queremos recuperar 
la representación del estado de un determinado recurso. _POST_ se utiliza cuando queremos cambiar el estado de un 
recurso.
              
              Cuando se desarrollan servicios Web, se debe preservar la semántica del GET
              Utilizar GET para otras operaciones, tales como eliminar, actualizar un recurso 
              es un abuso de la [Web y sus convenciones][2]
              POST es menos estricto que GET, es considerado como la operación wildcard.

Utilizar URI tunneling como estilo de Web API tiene como ventajas lo simple de su implementación, utilizar URIs 
para representar recursos, convierte a la API en Web-Friendly, y en caso de respetar la semántica del _GET_, se 
obtienen los beneficios que este método ofrece, a partir de sus propiedades de ser _seguro_ (no genera efectos
colaterales en el servidor) e _idempotente_ (invocar la operación repetidamente tiene el mismo efecto que hacerlo
una sola ves).Pero generalmente se utiliza para codificar operaciones en lugar de identificar 
recursos que pueden ser manipulados por métodos [HTTP][2].

A continuación un ejemplo de Web API utilizando URI Tunneling, codificando operaciones en las URI, 
sobrecargando el método _GET_, que en lugar de recuperar la representación del estado de un determinado recurso, 
es usado para realizar operaciones tales como, crear un recurso, actualizar un recurso, eliminar un recurso. 
Un ejemplo real de este tipo de APIs en la web es [Flickr][3] . Otro ejemplo similar es la API de [delicious][4]  .

El ejemplo es una simple WebAPI para manejar contactos, con operaciones sencillas, para agregar un Contacto, 
listar todos los contactos, recuperar los detalles de un contacto y finalmente dar debaja alguno contacto. 
El siguiente diagrama muestra la API de manera simplificada.

![alt text](http://yuml.me/6b2fcaf0 "ContactsAPI")

Contacts URI Tunneling Protocol (GET)
-------------------------------

<table>
<TR><TH>Verb</TH>         <TH>API </TH>     <TH>URI tunneling</TH></TR>
<TR><TD>GET</TD>         <TD>getContacts</TD>              <TD>/GetContacts</TD></TR>
<TR><TD>GET</TD>          <TD>addContact()</TD>    <TD>/AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>GET</TD>          <TD>getContactDetails()</TD>    <TD>/GetContactDetails?contactName={contactName}</TD></TR>
<TR><TD>GET</TD>       <TD>updateContact()</TD>    <TD>/UpdateContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>GET</TD>       <TD>cancelContact()</TD>    <TD>/CancelContact?contactName={contactName}</TD></TR>
</table>

                                  Tabla1: URI tunneling, no respetando la semántica de GET
Este tipo de URI se puede mapear de la siguiente manera

__http://localhost:8080/WebAPI/GetContactDetails?contactName=Roy__

<table>
<TR><TH>Service</TH>         <TH>Method</TH>     <TH>Argument</TH></TR>
<TR><TD>http://localhost:8080/WebAPI</TD>         <TD>/GetContactDetails</TD>              <TD>contactName=Roy</TD></TR>
</table>



Contacts URI Tunneling Protocol (GET,POST)
-------------------------------

<table>
<TR><TH>Verb</TH>         <TH>API </TH>     <TH>URI tunneling</TH></TR>
<TR><TD>GET</TD>         <TD>getContacts</TD>              <TD>/GetContacts</TD></TR>
<TR><TD>POST</TD>          <TD>addContact()</TD>    <TD>/AddContact<br/>
                                                          contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>GET</TD>          <TD>getContactDetails()</TD>    <TD>/GetContactDetails?contactName={contactName}</TD></TR>
<TR><TD>POST</TD>       <TD>updateContact()</TD>    <TD>/UpdateContact<br/>
                                                        contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>POST</TD>       <TD>cancelContact()</TD>    <TD>/CancelContact<br/>
                                                        contactName={contactName}</TD></TR>
</table>

                                  Tabla2: URI tunneling, respetando la semántica de GET

URI Tunneling, implementación utilizando Servlets
--------------------------------------------------
En esta implementación se usa un solo servlet y de acuerdo al
valor del pathInfo se termina llamando al metodo correspondiente, 
que realiza el parsing, en caso de ser neceario del queryString, 
para despues poder crear un objecto Contact.Otra alternativa seria crear un servlet por operación.
El codigo fuente de la implementacion del server en java y el cliente en cURL se puede obtener
utilizando el siguiente comando <<  git clone https://github.com/hexacta/URITunneling   >>, o
simplemente bajando el ZIP.


```java
public class ContactsAPIManager extends HttpServlet {

        ...
  public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String qString = req.getQueryString();
		String pathInfo = req.getPathInfo();

		if ("/AddContact".equals(pathInfo)) {
			this.addContact(qString, res);
		} else if ("/GetContacts".equals(pathInfo)) {
			this.getContacts(res);
		} else if ("/GetContactDetails".equals(pathInfo)) {
			this.getContactDetails(qString, res);
		} else if ("/UpdateContact".equals(pathInfo)) {
			this.updateContact(qString, res);
		}else if ("/CancelContact".equals(pathInfo)) {
			this.cancelOrder(qString, res);
		} else {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}
  ...
}

```
Mapeando métodos del Servlet a una determinada URI y creando el correspondiente
objeto de negocio a partir de la URI. En este caso particular, vemos como agregar
un contacto (artesanal),cuando se ejecuta la siguiente URI
  GET  /AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}

En el doGet (ver codigo anterior), de acuerdo al valor del pathInfo se realiza el correspondiente dispath.

```java
/**
   * GET	/AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}
	 * @param qString
	 * @param res
	 */
	private void addContact(String qString, HttpServletResponse res) {
		try {
			Contact contact = new Contact();
			Map<String, String> qValues = new HashMap<String, String>(10);

			String[] split = qString.split("&");
			LOG.info(split);
			for (String qparam : split) {
				String[] q = qparam.split("=");
				qValues.put(q[0], q[1]);
			}
			contact.setAddress(qValues.get("contactAddress"));
			contact.setCity(qValues.get("contactCity"));
			contact.setEmail(qValues.get("contactEmail"));
			contact.setName(qValues.get("contactName"));
			contact.setSkype(qValues.get("contactSkype"));
			contact.setState(qValues.get("contactState"));
			contact.setTwitter(qValues.get("contactTwitter"));
			contact.setZip(qValues.get("contactZip"));

			Repository.INSTANCE.addContact(contact);
			res.setContentType(APPLICATION_JSON);
			PrintWriter out = res.getWriter();
			out.print(new ContactRepresentation().asJSON(Repository.INSTANCE.retrieveContact(contact.getName())));
			res.setStatus(HttpServletResponse.SC_CREATED);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
```

API testing utilizando cURL
----------------------------
Agregar un Contacto
```
curl -i -H "Accept: application/json" "http://localhost:8080/ContactsAPILevel1/AddContact?contactName=Borg&contactAddress=Calle&contactCity=Roma&contactState=Roma&contactEmail=borg@tennis.com&contactTwitter=bborg&contactSkype=bborg"

HTTP/1.1 201 Created
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=ISO-8859-1
Content-Length: 164
Date: Tue, 22 May 2012 20:09:47 GMT

{"contact": {
  "name": "Borg",
  "address": "Calle",
  "city": "Roma",
  "state": "Roma",
  "email": "borg@tennis.com",
  "twitter": "bborg",
  "skype": "bborg"
}}
```

Listar todos los contactos

```
curl -i -H "Accept: application/json" http://localhost:8080/ContactsAPILevel1/GetContacts
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=ISO-8859-1
Content-Length: 340
Date: Tue, 22 May 2012 20:13:34 GMT

[{"contact": {
  "name": "Borg",
  "address": "Calle",
  "city": "Roma",
  "state": "Roma",
  "email": "borg@tennis.com",
  "twitter": "bborg",
  "skype": "bborg"
}}{"contact": {
  "name": "Vilas",
  "address": "Calle",
  "city": "Capital",
  "state": "Capital",
  "email": "vilas@tennis.com",
  "twitter": "gvilas",
  "skype": "gvilas"
}}]
```

TODO
----
* Implementar la estrategia de URI Tunneling, utilizando otros lenguages.
* Implementar clientes en diferentes lenguajes.
* Implementar la estrategia de URI Tunneling, utilizando solo POST.
* Implementar la estrategia de URI Tunneling, utilizando GET, POST cuando corresponda.




[1]: http://martinfowler.com/articles/richardsonMaturityModel.html        "Richardson"
[2]: http://www.w3.org/Protocols/rfc2616/rfc2616.html                     "HTTP"
[3]: http://www.flickr.com/services/api/                                  "Flickr"
[4]: http://delicious.com/developers                                      "delicious"