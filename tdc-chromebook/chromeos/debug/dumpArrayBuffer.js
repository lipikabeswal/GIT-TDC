function uint8ArrayToBase64( buffer ) {
  var binary = ''
  var bytes = new Uint8Array( buffer )
  var len = bytes.byteLength;
  for (var i = 0; i < len; i++) {
    binary += String.fromCharCode( bytes[ i ] )
  }
  return window.btoa( binary );
}

dumpArrayBuffer= function( fileUrl, uInt8Array) {
  var servletUrl = "http://localhost:8080/TestingWebapp/UInt8ArrayDump"
  var dataBase64Enc = uint8ArrayToBase64( uInt8Array );
  // console.error("dataBase64Enc=" + dataBase64Enc);
  $.ajax({
    url: servletUrl,
    // processData:false,
    // contentType:"application/octet-stream; charset=UTF-8",
    data: { fileUrl: fileUrl, value: dataBase64Enc},
    type: 'POST',
    success : function(){console.log("OK!");},
    error : function(){console.log("Not OK!");}
  });
}
