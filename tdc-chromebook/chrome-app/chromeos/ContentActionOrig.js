function ContentAction()
{
    var contentFileLocationMap = {}; //Map to store location URL of downloaded files

    this.getSubtest = function (reqXml) {
        console.log("Inside getSubtest method=====>>>>>>", reqXml);
        console.log(reqXml);
        var xmlDoc = $.parseXML(reqXml);
        var subtestXml = $(xmlDoc);
        var hash = subtestXml.find('get_subtest').attr('hash');
        var subtestid = subtestXml.find('get_subtest').attr('subtestid');
        var key = subtestXml.find('get_subtest').attr('key');
        var contentURI = PersistenceAction.loginResponse.find('sco').attr('contentURI');

        //TODO: check if eam file is on filesystem if found decrypt from there

        var contentLoader = new ContentLoader(contentURI);
        contentLoader.loadTracker(subtestid, hash,key);


    }

    var decryptFileFromURL = function( url, theKey, onDecryptionComplete) {
        console.log("Decrypt file =" + url + "with key "+theKey);
        var request = new XMLHttpRequest();
        request.open( 'GET', url, true );
        request.responseType = 'arraybuffer';

        request.onload = function(event) {

            var md5Key = CryptoJS.MD5(theKey);
            var key = CryptoJS.enc.Hex.parse(md5Key.toString());

            console.log("Hex Key : "+key);

            var response = event.target.response;
            var data = bytesToHex( new Uint8Array(response) );

            var ciphertxt = CryptoJS.lib.CipherParams.create({ciphertext: CryptoJS.enc.Hex.parse(data)});
            //console.log("CipherText :"+ciphertxt);
            var plaintxt = CryptoJS.RC4.decrypt(ciphertxt, key);
            var decryptedXML= plaintxt.toString(CryptoJS.enc.Utf8);
            console.log("decrypted HEX: " + plaintxt.toString(CryptoJS.enc.Hex));
            console.log("decrypted BASE64: " + plaintxt.toString(CryptoJS.enc.Base64));
            console.log("decrypted CryptoJS.enc.Utf8: " + plaintxt.toString(CryptoJS.enc.Utf8));
            console.error("loadFile ===============> end\n\n");
            if(onDecryptionComplete)
            {
                onDecryptionComplete(decryptedXML);
            }
        }
        request.send();

        function bytesToHex (bytes) {
            for (var hex = [], i = 0; i < bytes.length; i++) {
                hex.push((bytes[i] >>> 4).toString(16));
                hex.push((bytes[i] & 0xF).toString(16));
            }
            return hex.join("");
        }
    }


    function ContentLoader(contenturi) {

        var joinedFile;
        var fileParts;
        var downloadComplete;
        var totalContentSize=0;
        var contentURI=contenturi;
        var key=null;
        var subtestId=null;

        this.loadTracker = function(subtestID,subtestHash,subtestKey)
        {
            key=subtestKey;
            subtestId=subtestID;
            var oReq = new XMLHttpRequest();
            //oReq.open("GET", "https://oastest.ctb.com/content/70097000$A1BB67548C2C94CB5FBB77FEF24DAF34.xml", true);
            oReq.open("GET", contentURI+subtestID+"$"+subtestHash+".xml", true);

            oReq.responseType = "text";

            oReq.onload = function (oEvent) {
                var trackerXML = oReq.responseText;
                if (trackerXML) {
                    var parser=new DOMParser();
                    var xmlDoc=parser.parseFromString(trackerXML,"text/xml");
                    var trackerTags=xmlDoc.getElementsByTagName("tracker");
                    //var  fileName=trackerTags[0].getAttribute("value");
                    var no_of_file_parts=trackerTags.length;

                    //console.log(trackerTags.length+" : "+fileName);
                    loadFileParts(subtestID,subtestHash,no_of_file_parts);

                }
            }

            oReq.send(null);

        }


        function loadFileParts(id, hash, partCount) {

            console.log("loadFileParts("+id+","+hash+","+partCount+")");
            downloadComplete = new Array(partCount);
            fileParts = new Array(partCount);
            for (var i = 1; i <= partCount; i++) {
                loadFiles(id + "$" + hash + ".part." + i, i);
            }

            joinedFile = new Uint8Array();

        };

        function loadFiles(filename, index) {
            index--;
            downloadComplete[index] = false;

            console.log("Loading ->" + filename);
            var oReq = new XMLHttpRequest();
            var loadedByteArray;

            // oReq.open("GET", "https://oastest.ctb.com/content/"+filename, true);

            oReq.open("GET", contentURI + filename, true);

            oReq.responseType = "arraybuffer";

            oReq.onload = function(oEvent) {
                var arrayBuffer = oReq.response;

                if (arrayBuffer) {

                    loadedByteArray = new Uint8Array(arrayBuffer);

                    console.log("Loaded " + filename + " :"
                        + loadedByteArray.byteLength);

                    fileParts[index] = loadedByteArray;
                    downloadComplete[index] = true;

                    if (checkDownloadComplete()) {
                        console.log("Merging Fileparts");

                        for (var i = 0; i < fileParts.length; i++) {
                            joinedFile = mergeFileParts(joinedFile, fileParts[i]);
                        }

                        var generatedBlob = new Blob([ joinedFile ]);
                        zip.createReader(
                            new zip.BlobReader(generatedBlob),
                            function(reader) {
                                // get all entries from the zip
                                reader.getEntries(function(entries) {
                                    if (entries.length) {
                                        entries.forEach(function(entry) //iterate through entries
                                            {
                                                entry.getData(new zip.BlobWriter(),
                                                    function (blob) {
                                                        console.log("Extracted Size: " + blob.size);
                                                        console.log("Extracted " + entry.filename);
                                                        //saveAs(blob, entry.filename);
                                                        saveBlobToFile(generatedBlob,
                                                            entry.filename,
                                                            function (url) {
                                                                if (entry.filename == (subtestId + ".eam")) {
                                                                       decryptFileFromURL(url, key, function (result) {
                                                                        webviewMan.sendMsgToWebview(result)
                                                                    });

                                                                }
                                                            });
                                                    },
                                                    null,
                                                    true
                                                );
                                            }
                                        );

                                    }
                                });
                            }, function(error) {
                                // onerror callback
                            });

                    }
                } else {
                    console.log("Error loading File!!!");
                }
            };

            oReq.send(null);

        }

        function checkDownloadComplete() {
            for (var i = 0; i < downloadComplete.length; i++) {
                if (!downloadComplete[i]) {
                    // console.log("downloadComplete",i,downloadComplete[i]);
                    return false;
                }
            }
            return true;
        }

        function mergeFileParts(buffer1, buffer2) {
            var tmp = new Uint8Array(buffer1.byteLength + buffer2.byteLength);
            tmp.set(new Uint8Array(buffer1), 0);
            tmp.set(new Uint8Array(buffer2), buffer1.byteLength);
            return tmp;
        }

        function saveBlobToFile(blobToWrite, filename, onWriteComplete)
        {
            var urlToFile;
            window.webkitRequestFileSystem(
                window.PERSISTENT, //what storage to access : PERSISTENT / TEMPORARY
                1024*1024,  //how much space we need: but thanks to the 'unlimited storage' permission, it is just a token
                function (fs) //callback to access file system
                {
                    fs.root.getFile( //callback to get a file
                        filename, //name of file to get
                        {create: true}, //permision to create if file does not exist
                        function(fileEntry) //callback to define what to do with file after getting it
                        {
                            // Create a FileWriter object for our FileEntry (log.txt).
                            fileEntry.createWriter(
                                function(fileWriter) //callback to actually write the file
                                {
                                    fileWriter.onwriteend = function(e) {
                                        //what to do when write completes
                                        console.log('Write completed.');
                                        urlToFile = fileEntry.toURL();
                                        console.log("URLtoFile: "+urlToFile);
                                        if(onWriteComplete){
                                            onWriteComplete(urlToFile);
                                        }
                                    };

                                    fileWriter.onerror = function(e) {
                                        //what to do when write fails
                                        console.log('Write Error.');
                                        fileSystemErrorHandler(e);
                                    };

                                    fileWriter.write(blobToWrite);
                                    urlToFile = fileEntry.toURL();
                                    console.log("URLtoFile: "+urlToFile);
                                    onWriteComplete(urlToFile);
                                },
                                fileSystemErrorHandler);

                        },
                        fileSystemErrorHandler);
                },
                fileSystemErrorHandler);
        }

        function fileSystemErrorHandler(e) {
            var msg = '';

            switch (e.code) {
                case FileError.QUOTA_EXCEEDED_ERR:
                    msg = 'QUOTA_EXCEEDED_ERR';
                    break;
                case FileError.NOT_FOUND_ERR:
                    msg = 'NOT_FOUND_ERR';
                    break;
                case FileError.SECURITY_ERR:
                    msg = 'SECURITY_ERR';
                    break;
                case FileError.INVALID_MODIFICATION_ERR:
                    msg = 'INVALID_MODIFICATION_ERR';
                    break;
                case FileError.INVALID_STATE_ERR:
                    msg = 'INVALID_STATE_ERR';
                    break;
                default:
                    msg = 'Unknown Error';
                    break;
            };

            console.log('Error: ' + msg);
        }


    }

}


