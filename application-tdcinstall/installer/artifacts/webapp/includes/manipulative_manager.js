var scratchpadManager = (function(){
	var manipObject = {};
	var counter = 0;
	
	return{
	 saveStateManip: function(obj){
	 		obj = JSON.parse(obj);
	 		counter++;
	 		manipObject[obj.itemid] = new ManipObject(obj,counter);
	 	},	 	
	 getStateManip: function(){
	 	return manipObject;
	 },
	 getCounter: function(){
	 	return counter;
	 },
	 getObjectAtCounter: function(arg){
	     var objAtCount;
      	 for(var i in manipObject){
      	 	  	if(manipObject[i].sno == Number(arg)){
					 objAtCount = manipObject[i]; 
					 break;  		
		   		}
		}
	 	return objAtCount;
	 }		
	};
})();

function ManipObject(obj,sno){
this.toolid = obj.toolid;
this.state = obj.state;
this.x = obj.x;
this.y = obj.y;
this.sno = sno;
}