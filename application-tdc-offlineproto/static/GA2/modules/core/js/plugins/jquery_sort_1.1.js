/*
* jQuery Sort plugin
* Version 1.1.0 (4/1/09)
* @requires jQuery v1.2.3 or later
*
* Copyright (c) 2009 C. Pettit / ZeroPoint Development
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
*  
*/



(function($) {

    $.fn.sort = function(sortAttr, sortDesc) {
	//Must Specify Sort Attribute
        if (typeof (sortAttr) === "undefined") {
            return $(this);
        }
        if (sortAttr == "") {
            return $(this);
        }

	//If sort attribute is a single string such as "name"
        if (typeof (sortAttr) === "string") {

            var retObj = $(this).get().sort(function(a, b) {
		//Sort numeric values
                if (typeof ($(a).attr(sortAttr)) === "number") {

                    return parseInt($(a).attr(sortAttr),10) > parseInt($(b).attr(sortAttr),10) ? 1 : -1;
                }
		//sort string values
                else 
				{
					var value;

					var first = new String($(a).attr(sortAttr)).toLowerCase();
					var second = new String($(b).attr(sortAttr)).toLowerCase();

					if( first.search("grade ") > -1 && 
						first.search("-") > -1 && 
						second.search("grade ") > -1 && 
						second.search("-") > -1 )
					{
						first = parseInt(first.split("grade ")[1].split("-")[0],10);
						second = parseInt(second.split("grade ")[1].split("-")[0],10);
	                    value = first > second ? 1 : -1;
					}
					else if( first.search("grades ") > -1 && 
						first.search("-") > -1 && 
						second.search("grades ") > -1 && 
						second.search("-") > -1 )
					{
						first = parseInt(first.split("grades ")[1].split("-")[0],10);
						second = parseInt(second.split("grades ")[1].split("-")[0],10);
	                    value = first > second ? 1 : -1;
					}
					else
					{
						value = new String($(a).attr(sortAttr)).toLowerCase() > new String($(b).attr(sortAttr)).toLowerCase() ? 1 : -1
					}				
                    return value;
                }
            });
	    //If sort is descending
            if (getSort(sortDesc)) {
                return $(retObj.reverse());
            }
            else {
                return $(retObj);
            }
        }
	//If data is an object such as a returned JSON object
        if (typeof (sortAttr) === "object") {
	    //If the sort attribute is an Array  i.e. ["Name", "Phone","Foo"] , this will sort based on that order.
            if ((sortAttr).length) {
                var retObj = $(this).get().sort(function(a, b) {
                    var i = 0;
                    var retval = 1;
                    while (i < sortAttr.length) {
                        var al = $(a).attr(sortAttr[i]).toLowerCase();
                        var bl = $(b).attr(sortAttr[i]).toLowerCase();

                        if (al > bl) { retval = 1; break; }
                        if (bl > al) { retval = -1; break; }
                        i++;
                    }
                    return retval;

                });
                if (getSort(sortDesc)) {
                    return $(retObj.reverse());
                }
                else {
                    return $(retObj);
                }
            }
 	    //Sort object based on single sort attribute
            else {
                var retObj = $(this).get().sort(function(a, b) {
                    var attrLen = 0;
                    for (var v in sortAttr) {
                        var al = $(a).attr(v).toLowerCase();
                        var bl = $(b).attr(v).toLowerCase();
                        if (al > bl) { return (getSort(sortAttr[v])) ? -1 : 1; }
                        if (bl > al) { return (getSort(sortAttr[v])) ? 1 : -1; }
                    }

                });
                if (getSort(sortDesc)) {
                    return $(retObj.reverse());
                }
                else {
                    return $(retObj);
                }

            }
        }
    }

	//Determines if the sort should be Ascending(false) or Descending(true)
	//Can determine based on Boolean Value or String
    function getSort(sortDesc) {
        if (typeof sortDesc == "boolean") {
            return sortDesc;
        }
        else if (sortDesc.toLowerCase() == "desc") {
            return true;
        }
	//Incase boolean value gets passed as string
        else if (sortDesc.toLowerCase() == "true") {
            return true;
        }
        else return false;
    }
})(jQuery);
