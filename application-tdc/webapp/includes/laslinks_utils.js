function iframeLoaded(id, iframe){
for(var i=0; i<gController.lasAssetArray.length;i++){

console.log("iframe loaded",gController.lasAssetArray[i].asset.aw.iframeid);
console.log("Iframe autoplay** "+ i+" *"+ gController.lasAssetArray[i].data.getAttr('autoplay'))
console.log("Iframe autoplay** "+ i+" *"+gController.lasAssetArray[i].data.getAttr('playorder'))

if((gController.lasAssetArray[i].data.getAttr('playorder')== "1")){
iframe.contentWindow.play();
}
}
}

