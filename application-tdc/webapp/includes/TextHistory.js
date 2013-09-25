// TextHistory implemenation for undo/redo functionality in OpenLaszlo SWF runtime
// textfields.
var TextHistory = (function() {
    var matcher = new diff_match_patch();

    // Set to maximum of supported history entries +1, e.g. 6 for 5 entries
    var maxItems = 6;

    var history = {};

    /**
     * Called, when a textfield receives the focus in OpenLaszlo.
     * @param {integer} textId The id of the text component.
     */
    function _setFocus( textId ) {
        var hist = history[ textId ];
        if ( hist ) {
            // Update the button status
            var status = {};
            status.textId = textId;
            status.pointer = hist.pointer;
            status.canundo = _canUndo( hist );
            status.canredo = _canRedo( hist );

            // Pass the current history status into the OpenLaszlo app
            lzSetCanvasAttribute( "texthistoryupdate", JSON.stringify( status ) );
        } else {
            console.warn( "TextHistory._setFocus: Text component with ID %s not found!", textId );
        }
    }

    /**
     * Creates a new history object for a textfield. Called when a textfield
     * receives the focus in the OpenLaszlo app.
     * @param {integer} textId The id of the text component.
     * @param {string} origText The original version of the text.
     */
    function _createHistory( textId, origText ) {
        if ( ! history[ textId ] ) {
            var hist = history[ textId ] = {
                start: origText,
                pointer: -1,
                queue: []
            };
            // Now we need to add the default diff to the queue, which is
            // an unchanged version of the original text.
            var diff = matcher.diff_main( origText, origText );
            // Since this is the initial revision, the caret position will
            // always be 0.
            hist.queue.push( {diff: diff, caretIndex: 0} );
            hist.pointer++;
        }
    }

    /**
     * Creates the diff between the current version of the text and the original version.
     * Called whenever a new history entry should be generated.
     * @param {integer} textId The id of the text component.
     * @param {integer} caretIndex Insertion point or caret position.
     * @param {string} current The current version of the text.
     */
    function _createDiff( textId, caretIndex, current ) {
        // console.log("_createDiff: textId=%s, caretIndex=%s, current=%s", textId, caretIndex, current );
        if ( ! history[ textId ] ) {
            console.error( "TextHistory._createDiff: Cannot create diff, no history found for text with id=%s", textId );
        }
        var origText = history[ textId ].start;
        if ( ! origText && origText != "" ) {
            console.error( "Original text for comparison missing!" );
            console.error( "history[ '%s']=", textId, history[ textId ] );
            return;
        }
        var diff = matcher.diff_main( origText, current );
        if (diff.length == 0) {
            // console.log("No difference detected");
        } else {
            _addHistoryEntry( textId, caretIndex, diff );
        }
    }

    /**
     * Creates a new history action entry for a textfield.
     * @param {integer} textId The id of the text component.
     * @param {integer} caretIndex Insertion point or caret position.
     * @param {object} diff The diff object created by the diff_match_patch.diff_main() function.
     */
    function _addHistoryEntry( textId, caretIndex, diff ) {
        var hist = history[ textId ];
        if (textId && diff) {
            if ( hist.pointer < hist.queue.length - 1 ) {
                // In this situation the user has modified the text
                // and used the "undo" functionality to revert the
                // changes. The pointer does not point to the latest
                // change/version. To avoid an invalid history (creating
                // a branch), we have to remove all the items in the
                // history with an index value higher than the pointer.
                _discardEntriesAfterRedo( textId );
            }
            if ( hist.queue.length == maxItems ) {
                // We need to delete the oldest history entry
                hist.queue.shift();
            }
            hist.queue.push( {diff: diff, caretIndex: caretIndex} );
            if ( hist.pointer < maxItems - 1 ) {
               hist.pointer++;
           }
           var status = {};
           status.textId = textId;
           status.pointer = hist.pointer;
           status.canundo = _canUndo( hist );
           status.canredo = _canRedo( hist );

           // Pass the current history status into the OpenLaszlo app
           lzSetCanvasAttribute( "texthistoryupdate", JSON.stringify( status ) );
        }
    }

    /**
     * Undo an action for a text field.
     * @param {integer} textId The id of the text component.
     * @param {integer} caretIndex Insertion point or caret position.
     * @param {string} current The current text content.
     */
    function _undo( textId, caretIndex, current ) {
        // console.log( "_undo: textId=%s, caretIndex=%s, current=%s", textId, caretIndex, current );
        var hist = history[ textId ];
        if ( ! _canUndo( hist ) ) return;
        if ( _hasNewContent( textId, hist, current ) ) {
            _createDiff( textId, caretIndex, current );
        }
        var newText = null;
        if ( hist ) {
            var histEntry = null;
            if ( hist.pointer > -1 && hist.pointer <= hist.queue.length ) {
                histEntry = hist.queue[ hist.pointer - 1 ];
                var patches = matcher.patch_make( histEntry.diff );
                newText = matcher.patch_apply( patches, hist.start )[ 0 ];
                hist.pointer--;
            }
            // Transfer new value into OpenLaszlo app
            if ( newText != null ) {
                var status = _generateStatus( textId, histEntry.caretIndex, hist, newText, "undoCallback" );
                lzSetCanvasAttribute( "newtextvalue", status );
            }
        }
    }

    function _hasNewContent( textId, hist, current ) {
        // console.log( "_hasNewContent: textId=%s, hist=%s, current=%s", textId, hist, current );
        var result = false;
        if ( hist ) {
            var histEntry = null;
            if ( hist.pointer > -1 && hist.pointer <= hist.queue.length ) {
                histEntry = hist.queue[ hist.pointer ];
                var patches = matcher.patch_make( histEntry.diff );
                var latestFromHistory = matcher.patch_apply( patches, hist.start )[ 0 ];
                // console.error( "_hasNewContent: latestFromHistory |%s|", latestFromHistory );
                // console.error( "_hasNewContent: current |%s|", current );
                result = current !== latestFromHistory;
            }
        }
        // console.log( "_hasNewContent: result=" + result );
        return result;
    }

    /**
     * Redo an action for a text field.
     * @param {integer} textId The id of the text component.
     */
    function _redo( textId, current ) {
        var hist = history[ textId ];
        if ( ! _canRedo( hist ) ) return;
        var newText = null;
        if ( hist ) {
            var histEntry = null;
            if ( hist.pointer < hist.queue.length ) {
                histEntry = hist.queue[ hist.pointer + 1 ];
                var patches = matcher.patch_make( histEntry.diff );
                newText = matcher.patch_apply( patches, hist.start )[ 0 ];
                hist.pointer++;;
            }
            // Transfer new value into OpenLaszlo app
            if ( newText != null ) {
                var status = _generateStatus( textId, histEntry.caretIndex, hist, newText, "redoCallback" );
                lzSetCanvasAttribute( "newtextvalue", status );
            }
        }
    }

    /**
     * Generate the status object sent back into the OpenLaszlo app.
     * @param {integer} textId The id of the text component.
     * @param {object} hist The history object for a text field.
     * @param {string} newText The new text string.
     * @param {integer} methodName Name of the method to call on the TestFieldHistory instance.
     */
    function _generateStatus( textId, caretIndex, hist, newText, methodName ) {
        var status = {};
        status.textId = textId;
        status.caretIndex = caretIndex;
        status.text = newText;
        status.methodName = methodName;
        status.canundo = _canUndo( hist );
        status.canredo = _canRedo( hist );
        return JSON.stringify( status );
    }

    /**
     * Checks if the undo functionality is available.
     * @param {object} hist The history object.
     */
    function _canUndo( hist ) {
        var can = false;
        if ( hist && hist.pointer > 0 ) {
            can = true;
        }
        // console.log("_canUndo: " + can);
        return can;
    }

    /**
     * Checks if the redo functionality is available.
     * @param {object} hist The history object.
     */
    function _canRedo( hist ) {
        var can = false;
        // console.log("_canRedo: hist=", hist );
        if ( hist && hist.pointer < hist.queue.length - 1 ) {
            can = true;
        }
        return can;
    }

    /**
     * Checks if the redo functionality is available.
     * @param {integer} textId The id of the text component.
     */
    function _discardEntriesAfterRedo( textId ) {
        var hist = history[ textId ];
        if ( hist ) {
            while ( hist.pointer < hist.queue.length - 1 ) {
                hist.queue.pop();
            }
        }
    }

    /**
     * Clears the history.
     */
    function _clearHistory() {
        for ( var k in history ) {
            if ( history.hasOwnProperty( k ) ) {
                history[ k ] = null;
            }
        }
    }

    return {
        getMaxItems : function() {
            return this.maxItems;
        },
        createHistory : function( textId, origText ) {
            _createHistory( textId, origText );
        },
        getHistory : function() {
            return history;
        },
        setFocus : function( textId ) {
            _setFocus( textId );
        },
        createDiff : function( textId, caretIndex, current ) {
            _createDiff( textId, caretIndex, current );
        },
        addHistoryEntry : function( textId, diff ) {
            _addHistoryEntry(textId, diff);
        },
        undo : function( textId, caretIndex, current ) {
            _undo( textId, caretIndex, current );
        },
        redo : function( textId ) {
            _redo( textId );
        },
        clearHistory : function () {
            _clearHistory();
        },
        dumpHistory : function() {
            _dumpHistory();
        }

    };
})();
