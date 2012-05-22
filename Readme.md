URI Tunneling
=============

URI Tunneling, permite transferir información a través de los límites del sistema codificando la información 
en la URI misma. Web API´s que usan este estilo son categorizadas en el nivel 1 del modelo de [Richardson][1].
URI Tunneling, es una técnica simple y fácil de implementar, no se necesita de ningún framework especial 
para poder implementarlo. En Java por ejemplo podemos utilizar solamente Servlets.

En esta técnica requiere conocer:
* El template de las URIs
* El formato de la respuesta
* El comportamiento en cada interacción.

Generalmente se utilizan los métodos _GET_ y _POST_ según corresponda. _GET_ se utiliza cuando queremos recuperar 
la representación del estado de un determinado recurso. _POST_ se utiliza cuando queremos cambiar el estado de un 
recurso.
Utilizar URI tunneling como estilo de Web API tiene como ventajas lo simple de su implementación, utilizar URIs 
para representar recursos, convierte a la API en Web-Friendly, y en caso de respetar la semántica del _GET_, se 
obtienen los beneficios que este método ofrece, a partir de sus propiedades de ser _seguro_ e _idempotente_.

Pero la realidad indica que generalmente URI tunneling, se utiliza para codificar operaciones en lugar de identificar 
recursos que pueden ser manipulados por métodos HTTP.

A continuación un ejemplo de Web API utilizando URI Tunneling, codificando operaciones en las URI, 
además se muestra una mala utilización del método _GET_, que en lugar de recuperar la representación del 
estado de un determinado recurso, es usado para realizar operaciones tales como, crear un recurso, actualizar un recurso, eliminar un recurso. Un ejemplo de este tipo de API en la web es Flickr http://www.flickr.com/services/api/. Otro ejemplo similar es la API de delicious  http://delicious.com/developers

El ejemplo es una simple WebAPI para manejar contactos, con operaciones sencillas, para agregar un Contacto, 
listar todos los contactos, recuperar los detalles de un contacto y finalmente dar debaja alguno contacto. 
El siguiente diagrama muestra la API de manera simplificada.

![alt text](http://yuml.me/6b2fcaf0 "ContactsAPI")

Contacts URI Tunneling Protocol
-------------------------------

<table>
<TR><TH>Verb</TH>         <TH>API </TH>     <TH>URI tunneling</TH></TR>
<TR><TD>GET</TD>         <TD>getContacts</TD>              <TD>/GetContacts</TD></TR>
<TR><TD>GET</TD>          <TD>addContact()</TD>    <TD>/AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>GET</TD>          <TD>getContactDetails()</TD>    <TD>/GetContactDetails?contactName={contactName}</TD></TR>
<TR><TD>GET</TD>       <TD>updateContact()</TD>    <TD>/UpdateContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}</TD></TR>
<TR><TD>GET</TD>       <TD>cancelContact()</TD>    <TD>/CancelContact?contactName={contactName}</TD></TR>
</table>

Este tipo de URI se puede mapear de la siguiente manera

__http://localhost:8080/WebAPI/GetContactDetails?contactName=Roy__

<table>
<TR><TH>Service</TH>         <TH>Method</TH>     <TH>Argument</TH></TR>
<TR><TD>http://localhost:8080/WebAPI</TD>         <TD>/GetContactDetails</TD>              <TD>contactName=Roy</TD></TR>
</table>


Si bien las APIs que utilizan URI tunneling son consideradas de nivel 1, en este caso terminamos escribiendo una API 
de nivel 0. Lo mismo sucedería si utilizaramos POST. En este caso tenemos muchas URIs diferentes pero con un comportamiento
muy similar a las APIs de nivel 0.



[1]: http://martinfowler.com/articles/richardsonMaturityModel.html        "Richardson"