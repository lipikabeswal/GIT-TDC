LzHistory.receiveEvent = function(n, v){
    _root.Debug.write( '========== canvas attribute ', n );

    for ( var i = 0; i < v.length; i += 100 ) {
        _root.Debug.write( '     ' + v.substring( i, i + 100 ) );
    }

    _root.canvas[n] = v;
    _root.canvas['on' + n].sendEvent(v);
}
