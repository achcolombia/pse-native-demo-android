# PSE Móvil

PSE Móvil permite a los clientes que utilizan dispositivos móviles autorizar pagos desde una APP en vez de usar el navegador Web. Esto permite mayor control, recordar credenciales de forma segura y una experiencia nativa.

PSE Móvil puede ser invocado directamente desde las aplicaciones de los comercios y con esto mejorar notablemente la experiencia del usuario al mantener todo el proceso de compra dentro APPs.

Este repositorio es ejemplo y documentación del proceso para integrar la tecnología Browser2App (usada en PSE Móvil) con la APP del comercio.

# Validar que PSE Móvil está instalado

Se recomienda validar que la APP PSE Móvil esté instalada, para eso se debe chequear que el Intent asociado a la url "pseb2a://" sea aceptado por el dispositivo. Eso se puede probar con el siguiente código:

	Intent intent = null;
	try {
		intent = Intent.parseUri("pseb2a://openapp/", Intent.URI_INTENT_SCHEME);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}
	if (intent == null || intent.resolveActivity(getPackageManager()) == null) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Para continuar debes instalar PSE Móvil")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent(android.content.Intent.ACTION_VIEW);
						i.setData(Uri.parse("market://details?id=com.browser2app.pse"));
						startActivity(i);
					}
				});
		builder.create().show();
	}

Que en el caso de no encontrar la APP mostrará al usuario una ventana indicandole que debe instalarla y si presiona "OK" lo envía a Google Play a instalarla.
 
    
# Invocación

Antes de invocar la APP es necesario utilizar la API de ACH Colombia para generar un pago de manera estandar hasta el punto en que se obtiene la URL de redirección hacia el registro PSE que es de la forma

	https://registro.pse.com.co/PSEUserRegister/StartTransaction.htm?enc=XXXXXXXXXX


Al valor del parámetro "enc" de la url anterior le llamaremos ecus o cus encodeado.
 
Adicionalmente a eso se debe saber el identificador de la entidad financiera autorizadora y el tipo de cliente que ha sido seleccionado según los mismos códigos usados al momento de consultarle al usuario su banco, es decir:

Entidades financieras autorizadoras:
- 1040: BANCO AGRARIO
- 1052: BANCO AV VILLAS
- 1013: BANCO BBVA COLOMBIA S.A.
- 1032: BANCO CAJA SOCIAL
- 1019: BANCO COLPATRIA
- 1066: BANCO COOPERATIVO COOPCENTRAL
- 1006: BANCO CORPBANCA S.A
- 1051: BANCO DAVIVIENDA
- 1001: BANCO DE BOGOTA
- 1023: BANCO DE OCCIDENTE
- 1062: BANCO FALABELLA 
- 1012: BANCO GNB SUDAMERIS
- 1060: BANCO PICHINCHA S.A.
- 1002: BANCO POPULAR
- 1058: BANCO PROCREDIT
- 1007: BANCOLOMBIA
- 1061: BANCOOMEVA S.A.
- 1009: CITIBANK 
- 1014: HELM BANK S.A.
- 1507: NEQUI

Tipos de usuario:
- 0: Persona natural
- 1: Persona jurídica

Con estos datos se puede debe realizar la creación de una instancia de un autómata de pago. Eso se hace utilizando un webservice REST disponibilizado por ACH Colombia.

En este ejemplo se usa la biblioteca retrofit para la invocación del webservice. La firma del método es:

	@FormUrlEncoded
	@POST("automatonRequest/{id}")
	Call<AutomatonRequestResponse> automatonRequest(@Path("id") String id
			, @Field("userType") String userType
			, @Field("authorizerId") String authorizerId
			, @Field("cus") String cus
			, @Field("amount") String amount
			, @Field("subject") String subject
			, @Field("merchant") String merchant
			, @Field("paymentId") String paymentId
			, @Field("returnURL") String returnURL
			, @Field("cancelURL") String cancelURL
			, @Field("payerEmail") String payerEmail
	);
	
Todos los campos deben coincidir exáctamente con los utilizados al momento de crear el pago usando los webservices de ACH si algún campo no coincide la aplicación le mostrará un mensaje de error del tipo "Datos inconsistentes".

Lo mismo ocurre con el campo "merchant" que debe coincidir exáctamente con el nombre del comercio registrado en ACH Colombia, por ejemplo si el nombre del comercio es "ACH Colombia S.A." e intentamos iniciar un pago entregando en el parámetro "merchant" la cadena "ACH Colombia" el pago fallará con error de "Datos inconsistentes".	
	

La respuesta de ese webservice es un json con dos campos
	
	{
		"success": "true", //o "false" si falla.
		"AutomatonRequestId": "ID DE LA INSTANCIA DEL AUTÓMATA"
	}


Con esa respuesta se puede llamar a la APP PSE móvil.

	Intent intent = null;
	try {
		intent = Intent.parseUri("pseb2a://openapp/" + automatonRequestId(), Intent.URI_INTENT_SCHEME);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}
	if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
		startActivity(intent);
	}



Al finalizar el pago, PSE móvil direccionará al usuario hacia la URL de retorno asociada al pago. (Se puede usar para invocar la app del comercio de vuelta).

    