function m(d) {
    throw d;
}
var p = void 0,
    s = !0,
    t = null,
    v = !1;

function z(d) {
    return function () {
        return d
    }
}
if ("function" !== typeof Blob || "undefined" === typeof URL) var Blob = function (d) {
    var h = d.BlobBuilder || d.WebKitBlobBuilder || d.Qb || d.Pb || function (d) {
            function h(c) {
                E = !c
            }

            function q(c) {
                this.code = this[this.name = c]
            }

            function g(c, d, e) {
                this.data = c;
                this.size = c.length;
                this.type = d;
                this.encoding = e
            }

            function o() {
                this.data = []
            }

            function c(c) {
                return Object.prototype.toString.call(c).match(/^\[object\s(.*)\]$/)[1]
            }
            var k = o.prototype,
                e = g.prototype,
                f = d.Ob,
                x = "NOT_FOUND_ERR SECURITY_ERR ABORT_ERR NOT_READABLE_ERR ENCODING_ERR NO_MODIFICATION_ALLOWED_ERR INVALID_STATE_ERR SYNTAX_ERR".split(" "),
                w = x.length,
                r = d.URL || d.webkitURL || d,
                n = r.createObjectURL,
                i = r.revokeObjectURL,
                R = r,
                W = d.btoa,
                X = d.atob,
                E = v,
                u = d.Nb,
                y = d.Rb;
            for (g.vb = e.vb = s; w--;) q.prototype[x[w]] = w + 1;
            try {
                y && h.apply(0, new y(1))
            } catch (S) {}
            r.createObjectURL || (R = d.URL = {});
            R.createObjectURL = function (c) {
                var d = c.type;
                d === t && (d = "application/octet-stream");
                if (c instanceof g) {
                    d = "data:" + d;
                    return c.encoding === "base64" ? d + ";base64," + c.data : c.encoding === "URI" ? d + "," + decodeURIComponent(c.data) : W ? d + ";base64," + W(c.data) : d + "," + encodeURIComponent(c.data)
                }
                if (n) return n.call(r,
                    c)
            };
            R.revokeObjectURL = function (c) {
                c.substring(0, 5) !== "data:" && i && i.call(r, c)
            };
            k.append = function (d) {
                var e = this.data;
                if (y && (d instanceof u || d instanceof y))
                    if (E) e.push(String.fromCharCode.apply(String, new y(d)));
                    else {
                        e = 0;
                        for (d = (new y(d)).length; e < d; e++);
                    } else if (c(d) === "Blob" || c(d) === "File")
                    if (f) {
                        var h = new f;
                        e.push(h.readAsBinaryString(d))
                    } else m(new q("NOT_READABLE_ERR"));
                    else if (d instanceof g) d.encoding === "base64" && X ? e.push(X(d.data)) : d.encoding === "URI" ? e.push(decodeURIComponent(d.data)) : d.encoding ===
                    "raw" && e.push(d.data);
                else {
                    typeof d !== "string" && (d = d + "");
                    e.push(unescape(encodeURIComponent(d)))
                }
            };
            k.getBlob = function (c) {
                arguments.length || (c = t);
                return new g(this.data.join(""), c, "raw")
            };
            k.toString = z("[object BlobBuilder]");
            e.slice = function (c, d, e) {
                var f = arguments.length;
                f < 3 && (e = t);
                return new g(this.data.slice(c, f > 1 ? d : this.data.length), e, this.encoding)
            };
            e.toString = z("[object Blob]");
            return o
        }(d);
    return function (d, j) {
        var q = j ? j.type || "" : "",
            g = new h;
        if (d)
            for (var o = 0, c = d.length; o < c; o++) g.append(d[o]);
        return g.getBlob(q)
    }
}(self);
var aa = aa || navigator.Fb && navigator.Fb.bind(navigator) || function (d) {
        function h(g, h) {
            function o(c) {
                return function () {
                    if (u.readyState !== u.DONE) return c.apply(this, arguments)
                }
            }

            function j() {
                if (S || !D) D = E();
                M && (M.location.href = D);
                u.readyState = u.DONE;
                r()
            }

            function r() {
                l(u, ["writestart", "progress", "write", "writeend"])
            }

            function E() {
                var c = (d.URL || d.webkitURL || d).createObjectURL(g);
                w.push(c);
                return c
            }
            var u = this,
                y = g.type,
                S = v,
                D, M, V = {
                    create: s,
                    Ub: v
                }, Y;
            u.readyState = u.INIT;
            h || (h = "download");
            k ? (D = E(), c.href = D, c.Tb = h,
                q(c), u.readyState = u.DONE, r()) : (d.Sb && (y && "application/octet-stream" !== y) && (Y = g.slice || g.webkitSlice, g = Y.call(g, 0, g.size, "application/octet-stream"), S = s), e && "download" !== h && (h += ".download"), M = "application/octet-stream" === y || e ? d : d.open(), f) ? (x += g.size, f(d.TEMPORARY, x, o(function (c) {
                c.root.getDirectory("saved", V, o(function (c) {
                    function d() {
                        c.getFile(h, V, o(function (c) {
                            c.createWriter(o(function (d) {
                                d.onwriteend = function (d) {
                                    M.location.href = c.toURL();
                                    w.push(c);
                                    u.readyState = u.DONE;
                                    l(u, "writeend", d)
                                };
                                d.onerror =
                                    function () {
                                        var c = d.error;
                                        c.code !== c.ABORT_ERR && j()
                                };
                                ["writestart", "progress", "write", "abort"].forEach(function (c) {
                                    d["on" + c] = u["on" + c]
                                });
                                d.write(g);
                                u.abort = function () {
                                    d.abort();
                                    u.readyState = u.DONE
                                };
                                u.readyState = u.WRITING
                            }), j)
                        }), j)
                    }
                    c.getFile(h, {
                        create: v
                    }, o(function (c) {
                        c.remove();
                        d()
                    }), o(function (c) {
                        c.code === c.NOT_FOUND_ERR ? d() : j()
                    }))
                }), j)
            }), j)) : j()
        }

        function l(c, d, e) {
            for (var d = [].concat(d), f = d.length; f--;) {
                var g = c["on" + d[f]];
                if ("function" === typeof g) try {
                    g.call(c, e || c)
                } catch (h) {
                    j(h)
                }
            }
        }

        function j(c) {
            (d.Wb ||
                d.setTimeout)(function () {
                m(c)
            }, 0)
        }

        function q(c) {
            var e = g.createEvent("MouseEvents");
            e.initMouseEvent("click", s, v, d, 0, 0, 0, 0, 0, v, v, v, v, 0, t);
            c.dispatchEvent(e)
        }
        var g = d.document,
            o = d.URL || d.webkitURL || d,
            c = g.createElementNS("http://www.w3.org/1999/xhtml", "a"),
            k = "download" in c,
            e = d.webkitRequestFileSystem,
            f = d.requestFileSystem || e || d.Vb,
            x = 0,
            w = [],
            r = h.prototype;
        r.abort = function () {
            this.readyState = this.DONE;
            l(this, "abort")
        };
        r.readyState = r.INIT = 0;
        r.WRITING = 1;
        r.DONE = 2;
        r.error = r.onwritestart = r.onprogress = r.onwrite =
            r.onabort = r.onerror = r.onwriteend = t;
        d.addEventListener("unload", function () {
            for (var c = w.length; c--;) {
                var d = w[c];
                "string" === typeof d ? o.revokeObjectURL(d) : d.remove()
            }
            w.length = 0
        }, v);
        return function (c, d) {
            return new h(c, d)
        }
    }(self),
    A = 200;

function B(d, h) {
    var l, j, q;
    switch (d.nodeType) {
    case document.ELEMENT_NODE:
        l = document.createElementNS(d.namespaceURI, d.nodeName);
        if (d.attributes && 0 < d.attributes.length) {
            j = 0;
            for (q = d.attributes.length; j < q; j += 1) l.setAttribute(d.attributes[j].nodeName, d.getAttribute(d.attributes[j].nodeName))
        }
        if (h && d.childNodes && 0 < d.childNodes.length) {
            j = 0;
            for (q = d.childNodes.length; j < q; j += 1) l.appendChild(B(d.childNodes[j], h))
        }
        return l;
    case document.TEXT_NODE:
    case document.CDATA_SECTION_NODE:
    case document.COMMENT_NODE:
        return document.createTextNode(d.nodeValue)
    }
}
var C = {
    "0": "MSIE",
    1: "Chrome",
    2: "Safari",
    3: "Firefox"
};

function ba(d) {
    this.parent = d;
    var d = s,
        h;
    h = window.navigator.appName;
    var l = window.navigator.userAgent,
        j = l.match(/(opera|chrome|safari|firefox|msie)\/?\s*(\.?\d+(\.\d+)*)/i),
        l = l.match(/version\/([\.\d]+)/i);
    //(j && l) !== t && (j[2] = l[1]);
    h = j = j ? [j[1], j[2]] : [h, window.navigator.appVersion, "-?"];
    j = h[1].split(".");
    switch (h[0]) {
    case C[0]:
        9 > j[0] && (d = v);
        break;
    case C[1]:
        22 > j[0] && (d = v);
        break;
    case C[2]:
        5 > j[0] && (d = v);
        break;
    case C[3]:
        10 > j[0] && (d = v);
        break;
    default:
        d = v
    }
    d || this.parent.u("This browser version may not support all TI ExamCalc functionality. Fully supported browsers versions are listed at education.ti.com.");
    (this.tb = !! document.createElement("canvas").getContext) || this.parent.u("This browser version may not support all TI ExamCalc functionality. Fully supported browsers versions are listed at education.ti.com.")
}
window.onblur = function () {
    window.Ua = s
};
window.onfocus = function () {
    window.Ua = v
};
var F, G;

function ca(d) {
    this.na = this.ma = "";
    this.Cb = function (h) {
        var l, j, q = v,
            g, o, c = this;
        h ? "json" !== h.split(".").pop() ? d.u("The configuration file extension must be json.") : (l = new XMLHttpRequest, l.open("GET", h, s), j = setTimeout(function () {
            q = s;
            l.abort();
            d.u("Unable to connect with the server.")
        }, 5E3), l.onload = function () {
            if (!q)
                if (clearTimeout(j), this.status === A) {
                    try {
                        g = JSON.parse(this.responseText)
                    } catch (h) {
                        d.u("The configuration file is damaged, or is not a JSON file.");
                        return
                    }
                    d.$ = g.KeyHistBufferLength;
                    c.ma = g.FaceplateLocation;
                    c.na = g.FaceplateMobileLocation;
                    F = g.KeyMappingFile;
                    o = g.ROMLocation;
                    c.zb(o);
                    c.Bb()
                } else d.u("The configuration file cannot be found.")
        }, l.send(t)) : d.u("You must specify a configuration file.")
    };
    this.Bb = function () {
        var h;
        !this.ma || !this.na ? d.u("You must specify a URL for the faceplates.") : (h = this.ma.split(".").pop(), "svg" !== h ? d.u("The faceplate extension must be svg.") : (h = this.na.split(".").pop(), "svg" !== h ? d.u("The faceplate extension must be svg.") : navigator.userAgent.match(/Android/i) || navigator.userAgent.match(/webOS/i) ||
            navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPad/i) || navigator.userAgent.match(/iPod/i) || navigator.userAgent.match(/BlackBerry/i) || navigator.userAgent.match(/Windows Phone/i) || "ontouchstart" in window || 0 < navigator.msMaxTouchPoints ? this.$a(this.na) : this.$a(this.ma)))
    };
    this.$a = function (h) {
        var l, j, q, g, o, c, k, e;
        j = new XMLHttpRequest;
        l = setTimeout(function () {
            q = s;
            j.abort();
            d.u("Unable to connect with the server, or the requested faceplate is not available.")
        }, 5E3);
        j.open("GET", h, s);
        j.onreadystatechange =
            function () {
                if (!q && (clearTimeout(l), 4 === j.readyState))
                    if (this.status === A) {
                        o = j.responseXML.documentElement;
                        try {
                            o = document.importNode(o, s)
                        } catch (f) {
                            o = B(o, s)
                        }
                        g = document.getElementById("calculatorDiv");
                        e = o.getAttribute("width").indexOf("px");
                        c = parseFloat(o.getAttribute("width").substring(0, e));
                        e = o.getAttribute("height").indexOf("px");
                        k = parseFloat(o.getAttribute("height").substring(0, e));
                        g.style.width = c + "px";
                        g.style.height = k + "px";
                        g.appendChild(o)
                    } else d.u("The requested faceplate is not available.")
        };
        j.send()
    };
    this.zb = function (h) {
        var l = 15E4,
            j, q, g, o, c, k, e, f;
        l || (l = 15E3);
        h = h.substring(0, h.lastIndexOf("."));
        h += ".h84statej";
        j = new XMLHttpRequest;
        q = v;
        j.open("GET", h, s);
        c = setTimeout(function () {
            q = s;
            j.abort();
            d.u("Unable to connect with the server, or the requested ROM file is not available.");
            loadingStateFile = v
        }, l);
        j.setRequestHeader("Content-Type", "text/html;charset=utf-8");
        j.onload = function () {
            q ? loadingStateFile = v : (clearTimeout(c), this.status === A ? (g = this.responseText, o = 1E3, k = setInterval(function () {
                if (!window.Ua &&
                    (o -= 100, 0 >= o)) {
                    clearInterval(k);
                    G = [];
                    e = 0;
                    for (f = g.length; e < f; e += 2) G.push(parseInt(g.substr(e, 2), 16));
                    d.P();
                    loadingStateFile = v
                }
            }, 100)) : (h = "bin/No_Apps.h84statej", j.open("GET", h, s), j.send(), d.u("The requested state file is not available.")))
        };
        j.send()
    }
}

function da() {
    this.P()
}
da.prototype.P = function () {
    var d, h, l;
    this.ia = this.xa = this.ya = this.c = this.B = this.A = this.v = this.z = this.w = this.f = this.e = this.d = this.p = this.g = 0;
    this.M = [];
    this.ha = [];
    this.j = [];
    this.m = [];
    this.q = [];
    for (h = 0; 65536 > h; h++) this.M[h] = 0, this.ha[h] = 0, this.j[h] = 0, this.m[h] = 0, this.q[h] = 0;
    this.b = this.k = this.i = 0;
    this.a = Array(64);
    this.o = v;
    this.F = Array(16);
    for (d = 0; 16 > d; d++) {
        this.F[d] = Array(16);
        for (h = 0; 16 > h; h++) {
            this.F[d][h] = Array(16);
            for (l = 0; 16 > l; l++) this.F[d][h][l] = 0
        }
    }
    this.h = Array(16);
    for (d = 0; 4 > d; d++) {
        this.h[d] = Array(16);
        for (h = 0; 16 > h; h++) {
            this.h[d][h] = Array(16);
            for (l = 0; 16 > l; l++) this.h[d][h][l] = 0
        }
    }
    this.L = 255;
    this.Pa = this.Na = this.Sa = this.Ra = this.Oa = this.Qa = 0;
    this.Y = this.Ka = this.sa = this.wa = this.va = this.ga = this.fa = this.ua = this.ta = v;
    this.C = Array(2);
    this.Wa = new Date;
    this.C[0] = new ea;
    this.C[1] = new ea;
    for (h = 0; 64 > h; h++) this.a[h] = 0
};

function H(d) {
    d.va = 0 !== (d.a[10] & 4) ? s : v;
    d.wa = 0 !== (d.a[10] & 8) ? s : v
}

function I(d) {
    if (0 !== (d.a[44] & 1)) {
        if (d.C[0].X === v && (d.C[0].ab(35E4), d.C[0].bb()), d.C[1].X === v) d.C[1].ab(6E3), d.C[1].bb()
    } else d.C[0].cb(), d.C[1].cb()
}

function J(d) {
    d.a[40] &= 13
}

function K(d) {
    d.a[40] &= 11
}

function L(d) {
    d.k = ((d.a[9] & 7) << 4) + (d.a[8] & 14) >> 1;
    d.ta === s && (d.b += 1, d.ta = v);
    d.k = 0 !== d.k ? d.k - 1 : 63;
    d.d = 0;
    d.f = 8 + (d.k >>> 3);
    d.e = d.k % 8 << 1;
    d.h[d.d][d.f][d.e] = d.b & 15;
    d.h[d.d][d.f][d.e + 1] = (d.b & 240) >> 4;
    d.k = 0 !== d.k ? d.k - 1 : 63;
    d.d = 0;
    d.f = 8 + (d.k >>> 3);
    d.e = d.k % 8 << 1;
    d.h[d.d][d.f][d.e] = (d.b & 3840) >> 8;
    d.h[d.d][d.f][d.e + 1] = (d.b & 61440) >> 12;
    15 < d.k << 1 ? (d.a[9] = d.k >>> 3, d.a[8] = (d.k << 1) % 16) : (d.a[9] = 0, d.a[8] = d.k << 1)
}

function N(d) {
    return 8 <= d && 14 > d ? s : v
}

function O(d) {
    var h;
    h = (d.a[27] << 8) + (d.a[29] << 4) + d.a[28];
    0 !== (d.a[23] & 1) && (h = 0 !== (d.a[23] & 2) ? h - 1 : h + 1, d.a[27] = h >> 8, d.a[29] = h % 256 >> 4, d.a[28] = h % 256 % 16)
}

function P(d, h) {
    if (255 === d.L) d.a[h] = 0;
    else {
        switch ((d.L & 240) >> 4) {
        case 1:
            if (0 === (d.a[12] & 1)) {
                d.a[h] = 0;
                return
            }
            break;
        case 2:
            if (0 === (d.a[12] & 2)) {
                d.a[h] = 0;
                return
            }
            break;
        case 3:
            if (0 === (d.a[12] & 4)) {
                d.a[h] = 0;
                return
            }
            break;
        case 4:
            if (0 === (d.a[12] & 8)) {
                d.a[h] = 0;
                return
            }
            break;
        case 5:
            if (0 === (d.a[13] & 1)) {
                d.a[h] = 0;
                return
            }
            break;
        case 6:
            if (0 === (d.a[13] & 2)) {
                d.a[h] = 0;
                return
            }
            break;
        case 7:
            if (0 === (d.a[13] & 4)) {
                d.a[h] = 0;
                return
            }
        }
        switch (d.L & 15) {
        case 8:
            if (16 === h) {
                d.a[h] = 0;
                break
            }
            17 === h && (d.a[h] = 8);
            break;
        case 7:
            if (16 === h) {
                d.a[h] = 0;
                break
            }
            17 === h && (d.a[h] = 4);
            break;
        case 6:
            if (16 === h) {
                d.a[h] = 0;
                break
            }
            17 === h && (d.a[h] = 2);
            break;
        case 5:
            if (16 === h) {
                d.a[h] = 0;
                break
            }
            17 === h && (d.a[h] = 1);
            break;
        case 4:
            16 === h && (d.a[h] = 8);
            17 === h && (d.a[h] = 0);
            break;
        case 3:
            16 === h && (d.a[h] = 4);
            17 === h && (d.a[h] = 0);
            break;
        case 2:
            16 === h && (d.a[h] = 2);
            17 === h && (d.a[h] = 0);
            break;
        case 1:
            16 === h && (d.a[h] = 1);
            17 === h && (d.a[h] = 0);
            break;
        case 0:
            16 === h && (d.a[h] = 0), 17 === h && (d.a[h] = 8)
        }
    }
}

function Q(d) {
    var h, l, j, q, g, o, c, k, e, f, x, w, r;
    h = d.a[51];
    l = d.a[53];
    j = d.a[52];
    o = d.a[59];
    c = d.a[61];
    k = d.a[60];
    e = ((d.a[50] & 7) << 8) + (d.a[49] << 4) + d.a[48] >>> 0;
    f = d.a[56] & 7;
    r = 0;
    q = (h << 8) + (l << 4) + j >>> 0;
    g = (o << 8) + (c << 4) + k >>> 0;
    for (x = 0; x < e && N(h) !== s && N(h) !== s; x++) {
        w = r;
        r = 0;
        h = d.F[h][l][j];
        for (l = 0; l < f; l++) r += h & 1 << l - 1;
        r *= 1 << 8 - f;
        l = 1 << f;
        h = (h >>> 0) / l + w >>> 0;
        d.F[o][c][k] = h;
        q += 1;
        g += 1;
        h = q >>> 8;
        l = q % 256 >>> 4 >>> 0;
        j = q % 256 % 16;
        o = g >>> 8;
        c = g % 256 >>> 4 >>> 0;
        k = g % 256 % 16
    }
    0 !== (d.a[11] & 2) && 0 !== (d.a[58] & 8) && (d.sa = s, d.a[41] |= 1);
    d.a[58] &= 14
}

function ea() {
    this.X = this.da = v;
    this.ca = this.oa = 0;
    this.eb = function (d) {
        this.X && (this.ca -= d, 0 >= this.ca && (this.da = s, this.ca = this.oa))
    };
    this.reset = function () {
        this.ca = this.oa
    };
    this.bb = function () {
        this.X = s;
        this.ca = this.oa
    };
    this.cb = function () {
        this.X = v
    };
    this.ab = function (d) {
        this.oa = d
    }
}

function fa() {
    this.P()
}
fa.prototype.P = function () {
    this.Ya = [];
    this.n ? this.n.P() : this.n = new da
};

function ga(d) {
    var h = G,
        l, j, q, g;
    q = 0;
    j = h.length;
    for (l = 0; q < j; q += 2, l++) {
        g = h[q + 1] << 8 | h[q];
        d.n.M[l] = g;
        g = d.n;
        var o = d.n.M[l],
            c = l;
        g.ha[c] = (o & 65024) >>> 10;
        switch (g.ha[c]) {
        case 0:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 192) >>> 6;
            g.q[c] = (o & 768) >>> 8;
            break;
        case 22:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 240) >>> 4;
            g.q[c] = (o & 768) >>> 8;
            break;
        case 20:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 60:
        case 61:
        case 62:
        case 63:
            g.j[c] = (o & 4095) >>> 0;
            break;
        case 3:
            g.j[c] = (o & 15) >>> 0;
            g.q[c] = (o & 768) >>> 8;
            break;
        case 40:
        case 41:
        case 42:
        case 43:
            g.j[c] = (o & 4095) >>>
                0;
            break;
        case 16:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 14:
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 1:
            g.q[c] = (o & 768) >>> 8;
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 240) >>> 4;
            break;
        case 32:
        case 33:
        case 34:
        case 35:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 240) >>> 4;
            g.q[c] = (o & 3840) >>> 8;
            break;
        case 15:
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 5:
            g.q[c] = (o & 768) >>> 8;
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 240) >>> 4;
            break;
        case 30:
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 31:
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 10:
            g.m[c] = (o & 1008) >>> 4;
            break;
        case 11:
            g.m[c] = (o & 1008) >>>
                4;
            break;
        case 7:
            g.q[c] = (o & 768) >>> 8;
            g.m[c] = (o & 240) >>> 4;
            g.j[c] = (o & 15) >>> 0;
            break;
        case 17:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 21:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 2:
            g.j[c] = (o & 15) >>> 0;
            g.q[c] = (o & 768) >>> 8;
            break;
        case 6:
            g.m[c] = (o & 240) >>> 4;
            g.q[c] = (o & 768) >>> 8;
            break;
        case 18:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 19:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 23:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] =
                (o & 896) >>> 7;
            break;
        case 24:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 25:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 26:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 27:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 28:
            g.j[c] = (o & 15) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 29:
            g.j[c] = (o & 7) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 12:
            g.j[c] = (o & 3) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 8:
            g.j[c] = (o & 3) >>> 0;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 13:
            g.j[c] = (o & 3) >>> 0;
            g.m[c] = (o & 112) >>> 4;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 9:
            g.j[c] = (o & 3) >>> 0;
            g.q[c] = (o & 896) >>> 7;
            break;
        case 44:
        case 45:
        case 46:
        case 47:
            g.j[c] = (o & 4095) >>> 0;
            break;
        case 48:
        case 49:
        case 50:
        case 51:
            g.j[c] = (o & 4095) >>> 0;
            break;
        case 52:
        case 53:
        case 54:
        case 55:
            g.j[c] = (o & 4095) >>> 0;
            break;
        case 56:
        case 57:
        case 58:
        case 59:
            g.j[c] = (o & 4095) >>> 0;
            break;
        case 36:
        case 37:
        case 38:
        case 39:
            g.j[c] = (o & 4095) >>> 0
        }
    }
    d.n.b = 0;
    d.n.Qa = d.n.M[1] & 4095;
    d.n.Ra = d.n.M[2] & 4095;
    d.n.Sa = d.n.M[3] & 4095;
    d.n.Oa = d.n.M[4] & 4095;
    d.n.Na = d.n.M[5] & 4095;
    d.n.Pa = d.n.M[6] & 4095
}

function ha(d, h) {
    d.n.L = h;
    0 === d.n.L && (d.n.L = 255, d.n.a[47] |= 1, 0 !== (d.n.a[47] & 8) && (d.n.ua = s));
    ia(d, h) === s && (d.n.Ja = s, d.n.a[40] |= 8)
}

function ja(d) {
    d.n.a[11] & 1 && !(d.n.a[40] & 8) ? ha(d, d.Ya.shift()) : setTimeout(function () {
        ja(d)
    }, 100)
}

function ia(d, h) {
    if (d.n.fa && d.n.va || d.n.ga && d.n.wa) return v;
    switch ((h & 240) >> 4) {
    case 1:
        if (0 === (d.n.a[12] & 1)) return v;
        break;
    case 2:
        if (0 === (d.n.a[12] & 2)) return v;
        break;
    case 3:
        if (0 === (d.n.a[12] & 4)) return v;
        break;
    case 4:
        if (0 === (d.n.a[12] & 8)) return v;
        break;
    case 5:
        if (0 === (d.n.a[13] & 1)) return v;
        break;
    case 6:
        if (0 === (d.n.a[13] & 2)) return v
    }
    switch (h & 15) {
    case 0:
        return s;
    case 1:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[32] & 1)) return s;
        break;
    case 2:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[32] & 2)) return s;
        break;
    case 3:
        if (0 !== (d.n.a[11] &
            1) && 0 !== (d.n.a[32] & 4)) return s;
        break;
    case 4:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[32] & 8)) return s;
        break;
    case 5:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[33] & 1)) return s;
        break;
    case 6:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[33] & 2)) return s;
        break;
    case 7:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[33] & 4)) return s;
        break;
    case 8:
        if (0 !== (d.n.a[11] & 1) && 0 !== (d.n.a[33] & 8)) return s
    }
    return v
}

function T() {
    this.kb = "display";
    this.mb = 372;
    this.La = this.Ma = 0;
    this.lb = 48;
    this.J = {};
    this.P()
}
T.prototype.Fa = function (d) {
    this.J = d
};
T.prototype.P = function () {
    var d = document.getElementById("svg").getAttribute("viewBox"),
        h = document.getElementById("Display_Rect"),
        l, j, q, g = document.getElementById("displayDiv");
    q = d.split(/\s*,\s*|\s+/);
    d = parseFloat(q[0]);
    l = parseFloat(q[1]);
    j = parseFloat(q[2]);
    q = parseFloat(q[3]);
    g.style.top = null;
    g.style.top = 100 * ((h.getAttribute("y") - l) / q) + "%";
    g.style.left = null;
    g.style.left = 100 * ((h.getAttribute("x") - d) / j) + "%";
     g.style.height = null;
    g.style.height = h.getAttribute("height") * (100 / q) + "%";
     g.style.width = null;
    g.style.width = h.getAttribute("width") * (100 / j) + "%";
    this.canvas = document.getElementById(this.kb);
    this.context = this.canvas.getContext("2d")
};

function U() {
    this.Ba = this.enabled = v;
    this.J = {};
    this.Ta = "h30keymap";
    this.l[0] = {
        s: "KEY_2ND",
        code: 24,
        keyCode: [50],
        shiftKey: [s],
        r: 1,
        enabled: s
    };
    this.l[1] = {
        s: "KEY_MODE_QUIT",
        code: 72,
        keyCode: [77],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[2] = {
        s: "KEY_DELETE_INSERT",
        code: 88,
        keyCode: [46],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[3] = {
        s: "KEY_UPARROW",
        code: 104,
        keyCode: [38],
        shiftKey: [v],
        r: 6,
        enabled: s
    };
    this.l[4] = {
        s: "KEY_RIGHTARROW",
        code: 102,
        keyCode: [39],
        shiftKey: [v],
        r: 6,
        enabled: s
    };
    this.l[5] = {
        s: "KEY_LOG_10X",
        code: 23,
        keyCode: [76],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[6] = {
        s: "KEY_PRB_ANGLE",
        code: 40,
        keyCode: [82],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[7] = {
        s: "KEY_DATA_STAT",
        code: 56,
        keyCode: [68],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[8] = {
        s: "KEY_LEFTARROW",
        code: 103,
        keyCode: [37],
        shiftKey: [v],
        r: 6,
        enabled: s
    };
    this.l[9] = {
        s: "KEY_DOWNARROW",
        code: 101,
        keyCode: [40],
        shiftKey: [v],
        r: 6,
        enabled: s
    };
    this.l[10] = {
        s: "KEY_LN_EX",
        code: 22,
        keyCode: [78],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[11] = {
        s: "KEY_ND_UND",
        code: 39,
        keyCode: [70],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[12] = {
        s: "KEY_X10N_UND-ND",
        code: 55,
        keyCode: [222],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[13] = {
        s: "KEY_TABLE_FTOD",
        code: 71,
        keyCode: [65],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[14] = {
        s: "KEY_CLEAR",
        code: 87,
        keyCode: [8],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[15] = {
        s: "KEY_PI_HYP",
        code: 21,
        keyCode: [80],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[16] = {
        s: "KEY_SIN_SIN-1",
        code: 38,
        keyCode: [83],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[17] = {
        s: "KEY_COS_COS-1",
        code: 54,
        keyCode: [67],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[18] = {
        s: "KEY_TAN_TAN-1",
        code: 70,
        keyCode: [84],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[19] = {
        s: "KEY_DIVIDE_K",
        code: 86,
        keyCode: [111, 191],
        shiftKey: [v, v],
        r: 3,
        enabled: s
    };
    this.l[20] = {
        s: "KEY_CARET_XSQRT",
        code: 20,
        keyCode: [54],
        shiftKey: [s],
        r: 5,
        enabled: s
    };
    this.l[21] = {
        s: "KEY_X-1",
        code: 37,
        keyCode: [73],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[22] = {
        s: "KEY_LEFTPAREN_PERCENT",
        code: 53,
        keyCode: [57],
        shiftKey: [s],
        r: 5,
        enabled: s
    };
    this.l[23] = {
        s: "KEY_RIGHTPAREN_TOPERCENT",
        code: 69,
        keyCode: [48],
        shiftKey: [s],
        r: 5,
        enabled: s
    };
    this.l[24] = {
        s: "KEY_MULTIPLY",
        code: 85,
        keyCode: [106, 56],
        shiftKey: [v,
            s
        ],
        r: 3,
        enabled: s
    };
    this.l[25] = {
        s: "KEY_X2_SQRT",
        code: 19,
        keyCode: [88],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[26] = {
        s: "KEY_7",
        code: 36,
        keyCode: [103, 55],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[27] = {
        s: "KEY_8",
        code: 52,
        keyCode: [104, 56],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[28] = {
        s: "KEY_9",
        code: 68,
        keyCode: [105, 57],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[29] = {
        s: "KEY_MINUS_CONTRASTDOWN",
        code: 84,
        keyCode: [109, 189],
        shiftKey: [v, v],
        r: 3,
        enabled: s
    };
    this.l[30] = {
        s: "KEY_XYZTABC_CLEARVAR",
        code: 18,
        keyCode: [89],
        shiftKey: [v],
        r: 5,
        enabled: s
    };
    this.l[31] = {
        s: "KEY_4",
        code: 35,
        keyCode: [100, 52],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[32] = {
        s: "KEY_5",
        code: 51,
        keyCode: [101, 53],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[33] = {
        s: "KEY_6",
        code: 67,
        keyCode: [102, 54],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[34] = {
        s: "KEY_PLUS_CONTRASTUP",
        code: 83,
        keyCode: [107, 187],
        shiftKey: [v, s],
        r: 3,
        enabled: s
    };
    this.l[35] = {
        s: "KEY_STO_RECALL",
        code: 17,
        keyCode: [186],
        shiftKey: [s],
        r: 5,
        enabled: s
    };
    this.l[36] = {
        s: "KEY_1",
        code: 34,
        keyCode: [97, 49],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[37] = {
        s: "KEY_2",
        code: 50,
        keyCode: [98, 50],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[38] = {
        s: "KEY_3",
        code: 66,
        keyCode: [99, 51],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[39] = {
        s: "KEY_TOGGLE",
        code: 82,
        keyCode: [192],
        shiftKey: [s],
        r: 4,
        enabled: s
    };
    this.l[40] = {
        s: "KEY_0_RESET",
        code: 33,
        keyCode: [96, 48],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[41] = {
        s: "KEY_DECIMAL_COMMA",
        code: 49,
        keyCode: [110, 190],
        shiftKey: [v, v],
        r: 4,
        enabled: s
    };
    this.l[42] = {
        s: "KEY_NEGATIVE_ANS",
        code: 65,
        keyCode: [189],
        shiftKey: [s],
        r: 4,
        enabled: s
    };
    this.l[43] = {
        s: "KEY_ENTER",
        code: 81,
        keyCode: [13],
        shiftKey: [v],
        r: 4,
        enabled: s
    }
}
U.prototype = new function () {
    this.D = p;
    this.Z = v;
    this.aa = [];
    this.$ = this.ba = 0;
    this.H = this.za = v;
    this.ub = "transparent";
    this.N = p;
    this.T = {
        "0": "SCREEN_OPERATION_KEYS",
        1: "SECOND_KEY",
        2: "ALPHA_KEY",
        3: "ARITMETHIC_OPERATORS_KEYS",
        4: "NUMBER_KEYS",
        5: "MORE_MATH_OPERATOR_KEYS",
        6: "ARROW_KEYS",
        7: "NO_GROUP"
    };
    this.R = {
        qb: p,
        rb: p,
        fb: p,
        gb: p,
        pb: p,
        nb: p,
        hb: p,
        ob: p
    };
    this.Ia = {
        ib: "Button has no Fill Attribute",
        jb: "Button has no Stroke Attribute or its Fill is Transparent"
    };
    this.l = [];
    this.t = [];
    this.I = [];
    this.S = [];
    this.U = [];
    this.K = [];
    this.ra = [];
    this.Aa = "red";
    this.Jb = "orange";
    this.W = [];
    this.la = [];
    this.ja = [];
    this.ka = [];
    this.V = "normal";
    this.Ga = v;
    this.Ha = function (d) {
        this.aa[this.ba] = d;
        this.ba = (this.ba + 1) % this.$
    };
    this.Xa = function () {
        this.ba = this.aa.length = 0
    };
    this.yb = function () {
        var d = 0,
            h = [],
            d = [],
            l = [];
        return this.aa.length === this.$ && 0 !== this.ba ? (d = this.ba, h = this.aa.slice(0, d), d = this.aa.slice(d), l.concat(d, h)) : this.aa
    };
    this.Db = function (d) {
        var h = d.currentTarget || d.target || d.srcElement;
        this.Ga && (d.stopPropagation(), d.preventDefault(),
            this.N.focus(), !this.H && this.ea(h.id) && (this.D = h, this.H = this.Z = s, this.Q(h.id, this.Aa), d = this.t.indexOf(h.id), this.pa(this.U[d]), this.Ha(h.id)))
    };
    this.Za = function (d) {
        var h = d.currentTarget || d.target || d.srcElement,
            l = -1;
        d.preventDefault();
        this.H && (this.D === h && this.ea(h.id)) && (l = this.t.indexOf(h.id), this.Q(h.id, this.R[this.T[this.l[l].r]]), this.D = t, this.H = v, this.qa(h.id))
    };
    this.Eb = function (d) {
        var h = d.currentTarget || d.target || d.srcElement;
        this.H && this.D === h && this.Za(d)
    };
    this.Q = function (d, h) {
        var l;
        l = this.ra[this.t.indexOf(d)];
        l !== p && l.setAttribute("fill", h)
    };
    this.O = function (d, h) {
        var l, j, q;
        l = function (g) {
            return -1 === d.indexOf(g)
        };
        j = function (d) {
            this.Q(d, this.Jb);
            return s
        };
        q = function (d) {
            this.Q(d, this.R[this.T[this.l[this.t.indexOf(d)].r]]);
            return s
        };
        d && (h ? (d.forEach(q, this), this.W = this.W.filter(l, this)) : (d.forEach(j, this), this.W = d));
        q = j = l = t
    };
    this.xb = function (d) {
        for (var h = -1, l = 0, j = d.getElementsByTagName("path"), q, g, o, h = j.length; l < h; l += 1)
            if (j[l].hasAttribute("fill")) {
                g = j[l].getAttribute("fill");
                if (g !== this.ub && j[l].hasAttribute("stroke")) {
                    q =
                        j[l];
                    break
                }
                o = this.Ia.jb
            } else o = this.Ia.ib;
        q === p && m(Error("Couldn't locate a suitable button: " + o));
        h = this.t.indexOf(d.id); - 1 !== h && (this.ra[h] = j[l]);
        return q
    };
    this.Ab = function (d) {
        var h, l, j, q, g, o, c, k = this,
            e, f, x;
        this.za && this.wb();
        this.t = this.l.map(function (c) {
            return c.s
        });
        this.I = this.l.map(function (c) {
            return c.keyCode[0]
        });
        this.S = this.l.map(function (c) {
            return c.keyCode[1]
        });
        this.U = this.l.map(function (c) {
            return c.code
        });
        this.N = document.getElementById("calculatorDiv");
        this.N.onkeydown = function (c) {
            k.Kb(c)
        };
        this.N.onkeyup = function (c) {
            k.Lb(c)
        };
        this.N.onmousedown = function () {
            k.N.focus()
        };
        this.N.onblur = function () {
            k.Mb()
        };
        this.N.oncontextmenu = function (c) {
            c.preventDefault()
        };
        q = function (c) {
            k.Db(c)
        };
        g = function (c) {
            k.Za(c)
        };
        o = function (c) {
            k.Eb(c)
        };
        c = function (c) {
            1 === c.targetTouches.length && c.preventDefault()
        };
        window.navigator.msPointerEnabled ? e = 0 : "ontouchstart" in window ? (e = 1, "ontouchleave" in window && (f = s), window.hasOwnProperty("ontouchstart") || (x = s)) : e = 2;
        h = this.t.length;
        for (j = 0; j < h; j += 1) {
            this.K.push(document.getElementById(this.t[j]));
            try {
                l = this.xb(this.K[j]), color = l.getAttribute("fill"), this.R[this.T[this.l[j].r]] === p && (this.R[this.T[this.l[j].r]] = l.getAttribute("fill"))
            } catch (w) {
                d.u("Button was undefined for key " + this.t[j] + ": " + w.message)
            }
            switch (e) {
            case 0:
                this.K[j].addEventListener("MSPointerDown", q);
                this.K[j].addEventListener("MSPointerUp", g);
                this.K[j].addEventListener("MSPointerOut", o);
                break;
            case 1:
                if (this.K[j].addEventListener("touchstart", q), this.K[j].addEventListener("touchmove", c), this.K[j].addEventListener("touchend",
                    g), f && this.K[j].addEventListener("touchleave", o), !x) break;
            default:
                this.K[j].onmousedown = q, this.K[j].onmouseup = g, this.K[j].onmouseout = o
            }
        }
        this.za = s
    };
    this.wb = function () {
        this.t.length = 0;
        this.I.length = 0;
        this.S.length = 0;
        this.U.length = 0;
        this.K.length = 0;
        this.ra.length = 0;
        this.W.length = 0;
        this.D = p;
        this.Z = v;
        this.Xa();
        this.za = this.H = v;
        this.V = "normal";
        this.R = {
            qb: p,
            rb: p,
            fb: p,
            gb: p,
            pb: p,
            nb: p,
            hb: p,
            ob: p
        }
    };
    this.Mb = function () {
        var d = 0,
            h = 0,
            l = 0,
            j = v,
            q, g;
        document.onhelp = z(s);
        if (this.D && this.H)
            if (this.D.id) q = this.D.id, d = this.t.indexOf(q),
        this.Q(q, this.R[this.T[this.l[d].r]]), this.D = t, this.H = v, this.qa(q);
        else {
            for (g = this.D; !j && -1 !== d;) {
                d = this.I.indexOf(g.keyboardCode, h);
                if (-1 !== d && this.l[d].shiftKey[0] === g.shiftKey) {
                    q = this.t[d];
                    j = s;
                    break
                }
                if (d === this.I.length - 1) {
                    d = -1;
                    break
                }
                h = d + 1
            }
            j || (l = 1, d = h = 0);
            for (; !j && -1 !== d;) {
                d = this.S.indexOf(g.keyboardCode, h);
                if (-1 !== d && this.l[d].shiftKey[l] === g.shiftKey) {
                    q = this.t[d];
                    j = s;
                    break
                }
                if (d === this.I.length - 1) {
                    d = -1;
                    break
                }
                h = d + 1
            } - 1 !== d && j && (this.Q(q, this.R[this.T[this.l[d].r]]), this.D = t, this.H = v, this.qa(q))
        }
    };
    this.Lb = function (d) {
        var h, l = 0,
            j, q = 0,
            g = 0,
            o = v;
        if (Z === v && this.H && 9 !== d.keyCode) {
            h = this.Va(d);
            if (18 === h.keyboardCode || 91 === h.keyboardCode || 16 === h.keyboardCode) h = this.D;
            for (; !o && -1 !== l;) {
                l = this.I.indexOf(h.keyboardCode, g);
                if (-1 !== l && this.l[l].shiftKey[0] === h.shiftKey) {
                    j = this.t[l];
                    o = s;
                    break
                }
                if (l === this.I.length - 1) {
                    l = -1;
                    break
                }
                g = l + 1
            }
            o || (q = 1, l = g = 0);
            for (; !o && -1 !== l;) {
                l = this.S.indexOf(h.keyboardCode, g);
                if (-1 !== l && this.l[l].shiftKey[q] === h.shiftKey) {
                    j = this.t[l];
                    o = s;
                    break
                }
                if (l === this.I.length - 1) {
                    l = -1;
                    break
                }
                g =
                    l + 1
            }
            o && (this.D.hasOwnProperty("keyboardCode") && this.D.keyboardCode === h.keyboardCode && this.D.hasOwnProperty("shiftKey") && this.D.shiftKey === h.shiftKey && this.ea(j)) && (d.preventDefault(), this.Q(j, this.R[this.T[this.l[l].r]]), this.D = t, this.H = v, this.qa(j))
        }
    };
    this.Kb = function (d) {
        var h = -2,
            l = 0,
            j, q = 0,
            g = v;
        if (this.Ga) {
            for (j = this.Va(d); !g && -1 !== h;) h = this.I.indexOf(j.keyboardCode, q), -1 !== h && (this.l[h].shiftKey[l] === j.shiftKey && this.ea(this.t[h])) && (d.preventDefault(), document.onhelp = z(v), !this.H && Z === v && (16 !==
                d.keyCode && 9 !== d.keyCode) && (this.D = j, this.Q(this.t[h], this.Aa), this.pa(this.U[h]), this.Ha(this.t[h]), g = this.Z = this.H = s)), h === this.I.length - 1 ? h = -1 : q = h + 1;
            g || (q = 0, h = -2, l = 1);
            for (; !g && -1 !== h;) h = this.S.indexOf(j.keyboardCode, q), -1 !== h && (this.l[h].shiftKey[l] === j.shiftKey && this.ea(this.t[h])) && (d.preventDefault(), document.onhelp = z(v), !this.H && Z === v && (16 !== d.keyCode && 9 !== d.keyCode) && (this.D = j, this.Q(this.t[h], this.Aa), this.pa(this.U[h]), this.Ha(this.t[h]), g = this.Z = this.H = s)), h === this.I.length - 1 ? h = -1 : q = h +
                1
        }
    };
    this.Va = function (d) {
        var h = d.keyCode,
            l = d.shiftKey,
            j = d.keyIdentifier,
            d = d.location || d.keyLocation;
        switch (h) {
        case 59:
            h = 186;
            break;
        case 61:
            h = 187;
            3 === d && (l = v);
            break;
        case 96:
            h = 48;
            break;
        case 97:
            h = 49;
            break;
        case 98:
            h = 50;
            break;
        case 99:
            h = 51;
            break;
        case 100:
            h = 52;
            break;
        case 101:
            h = 53;
            break;
        case 102:
            h = 54;
            break;
        case 103:
            h = 55;
            break;
        case 104:
            h = 56;
            break;
        case 105:
            h = 57;
            break;
        case 106:
            h = 56;
            l = s;
            break;
        case 107:
            h = 187;
            l = s;
            break;
        case 109:
            h = 189;
            break;
        case 110:
            h = 190;
            break;
        case 111:
            h = 191;
            break;
        case 173:
            h = 189;
            break;
        case 187:
            3 ===
                d && (l = "U+002B" === j ? s : v);
            break;
        case 224:
            h = 91
        }
        return {
            keyboardCode: h,
            shiftKey: l
        }
    };
    this.ea = function (d) {
        return 0 === this.W.length || -1 === this.W.toString().indexOf(d)
    };
    this.Ib = function () {
        var d, h, l, j = [],
            q, g = this;
        F || m(Error("Unable to connect with the server, or the requested key mapping file is not available."));
        if (F.split(".").pop() === this.Ta) {
            d = new XMLHttpRequest;
            l = v;
            h = setTimeout(function () {
                    l = s;
                    d.abort();
                    m(Error("Unable to connect with the server, or the requested key mapping file is not available."))
                },
                5E3);
            d.open("GET", F, s);
            d.onreadystatechange = function () {
                if (4 !== d.readyState || l) return v;
                clearTimeout(h);
                if (200 === d.status) {
                    try {
                        j = JSON.parse(d.responseText)
                    } catch (c) {
                        m(Error("The key mapping file is damaged or not a valid key mapping file."))
                    }
                    0 < j.length && (j.forEach(function (c) {
                        q = this.U.indexOf(c.code);
                        if (-1 !== q) {
                            for (a = 0; 2 > a; a += 1)
                                if (b = this.I.indexOf(c.keyCode[a]), -1 !== b && this.l[b].shiftKey[0] === c.shiftKey[0] && (delete this.I[b], delete this.l[b].keyCode[0], delete this.l[b].shiftKey[0]), b = this.S.indexOf(c.keyCode[a]), -1 !== b && this.l[b].shiftKey[1] === c.shiftKey[1]) delete this.S[b], delete this.l[b].keyCode[1], delete this.l[b].shiftKey[1];
                            this.l[q].keyCode = c.keyCode;
                            this.I[q] = c.keyCode[0];
                            this.S[q] = c.keyCode[1];
                            this.l[q].shiftKey = c.shiftKey
                        }
                    }, g), g.l.forEach(function (c) {
                        !c.keyCode[0] && !c.keyCode[1] && m(Error("The key " + c.s + " doesn't have a keyboard code associated."))
                    }))
                } else g = t, 404 === d.status && m(Error("Unable to connect with the server, or the requested key mapping file is not available."));
                g = j = t
            };
            try {
                d.send(t)
            } catch (o) {
                m(Error(o.message))
            }
        } else m(Error("The key mapping file extension must be " +
            this.Ta))
    };
    this.disableKeys = function (d) {
        if (Z === v) {
            var d = d.trim(),
                h = window.location.host,
                l = d.split("/"),
                j, q, g = this;
            if (0 === d.indexOf("http://") || 0 === d.indexOf("https://")) {
                if ("json" === d.split(".").pop()) {
                    if (l[2] === h) {
                        h = new XMLHttpRequest;
                        h.open("GET", d + "?r=" + Math.random(), v);
                        h.send(t);
                        if (h.status === A) {
                            try {
                                j = JSON.parse(h.responseText), j.keys && (j.hasOwnProperty("secondKeys") && j.hasOwnProperty("alphaKeys") && j.keys instanceof Array && j.secondKeys instanceof Array && j.alphaKeys instanceof Array) && (q = function (c) {
                                    c =
                                        g.U.indexOf(c);
                                    if (-1 !== c) return g.t[c]
                                }, g.la = j.keys.map(q, g), g.ja = j.secondKeys.map(q, g), g.ka = j.alphaKeys.map(q, g), g.O(g.t, s), "2nd" !== g.V && "alpha" !== g.V && g.O(g.la, v), "2nd" === g.V && g.O(g.ja, v), "alpha" === g.V && g.O(g.ka, v))
                            } catch (o) {
                                g.O(g.t, s), g = t, m(Error("You must provide a path to a valid key configuration file"))
                            }
                            g = t;
                            return s
                        }
                        g = t;
                        m(Error("Unable to connect with the server, or the requested key mapping file is not available."))
                    }
                    m(Error("The requested file must be in the same server as the TI ExamCalc application"))
                }
                m(Error("The key configuration file extension must be .json"))
            }
            m(Error("You must provide a valid URL beginning with http:// or https://"))
        }
        m(Error("The keys cannot be disabled when the calculator is hidden."))
    };
    this.enableAllKeys = function () {
        Z === v ? (this.la.length = 0, this.ja.length = 0, this.ka.length = 0, this.O(this.t, s)) : m(Error("The keys cannot be enabled when the calculator is hidden."))
    };
    this.disableAllKeys = function () {
        Z === v ? this.O(this.t, v) : m(Error("The keys cannot be disabled when the calculator is hidden."))
    };
    this.qa = function (d) {
        d = (this.Ba = this.l[0].s === d && !this.Ba) ? "2nd" : "normal";
        Z === v && this.V !== d && (this.O(this.W, s), "2nd" === d ? this.O(this.ja, v) : "alpha" === d ? this.O(this.ka, v) : this.O(this.la, v), this.V = d)
    };
    this.pa =
        z(s);
    this.sb = function () {
        this.Ga = s
    }
};
U.prototype.Fa = function (d) {
    this.J = d
};
U.prototype.pa = function (d) {
    var h = this.J;
    h.Ya.push(d);
    ja(h)
};
var Z = v;
TI30 = function (d) {
    this.u = function (d) {
        console.log("TI Default Error Handler: " + d)
    };
    new ba(this);
    (new ca(this)).Cb(d)
};
TI30.prototype.P = function () {
    var d, h, l;
    this.Ca = this.Ca || new T;
    if (!this.G) {
        this.G = new U;
        try {
            this.G.Ab(this)
        } catch (j) {
            this.u(j.message)
        }
    }
    l = parseInt(this.$, 10);
    isNaN(l) ? (this.u("Unable to read a key history buffer length value. A value of 100 will be set."), this.G.$ = 100) : this.G.$ = l;
    this.J ? this.J.P() : this.J = new fa;
    this.G.sb();
    this.G.Fa(this.J);
    this.Ca.Fa(this.J);
    this.scale || (this.scale = 1, this.N = document.getElementById("calculatorDiv"), this.Hb = this.N.getBoundingClientRect().width, this.Gb = this.N.getBoundingClientRect().height);
    ga(this.J);
    h = this;
    this.Ea = this.Ea || 0;
    this.Da = this.Da || t;
    d = function () {
        var g = h.J.n,
            o;
        for (o = 3E3; o >= 0; o--) {
            var c = g,
                k = p,
                e = p,
                f = p,
                k = k = p;
            switch (c.ha[c.b]) {
            case 0:
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 2:
                    c.i = c.i + 2;
                    c.g = c.j[c.b];
                    c.b = c.g !== 0 ? c.b + c.a[c.g] + 1 : c.b + 1;
                    break;
                case 3:
                    c.i = c.i + 3;
                    c.k = (c.a[9] & 7) * 16 + (c.a[8] & 14) >>> 1;
                    c.d = 0;
                    c.f = 8 + (c.k >>> 3);
                    c.e = c.k % 8 * 2;
                    c.b = c.h[c.d][c.f][c.e + 1] * 4096 + c.h[c.d][c.f][c.e] * 256;
                    c.k = c.k + 1;
                    c.d = 0;
                    c.f = 8 + (c.k >>> 3);
                    c.e = c.k % 8 << 1;
                    c.b = c.h[c.d][c.f][c.e + 1] * 16 + c.h[c.d][c.f][c.e] + c.b;
                    c.k = c.k + 1;
                    if (c.k >
                        63) {
                        c.a[9] = 0;
                        c.a[8] = 0
                    } else if (c.k << 1 > 15) {
                        c.a[9] = c.k >>> 3;
                        c.a[8] = (c.k << 1) % 16
                    } else {
                        c.a[9] = 0;
                        c.a[8] = c.k << 1
                    }
                    break;
                case 0:
                    c.i = c.i + 1;
                    k = c.m[c.b];
                    if (k === 0) c.b = c.b + 1;
                    else if (k === 2) c.ta = s
                }
                break;
            case 1:
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 0:
                    c.i = c.i + 2;
                    c.ia = (c.a[7] << 12) + (c.a[6] << 8) + (c.a[5] << 4) + c.a[4];
                    c.ya = c.M[c.ia];
                    e = (c.ya & 240) >> 4;
                    c.e = c.a[2] | 1;
                    c.f = c.a[3];
                    c.d = c.a[0] >>> 2;
                    c.h[c.d][c.f][c.e] = e;
                    e = c.ya & 15;
                    c.h[c.d][c.f][c.e - 1] = e;
                    break;
                case 1:
                    c.i = c.i + 2;
                    c.ia = (c.a[7] << 12) + (c.a[6] << 8) + (c.a[5] << 4) + c.a[4];
                    c.xa = c.M[c.ia];
                    e = (c.xa &
                        61440) >> 12;
                    c.e = c.a[2] | 1;
                    c.f = c.a[3];
                    c.d = c.a[0] >>> 2;
                    c.h[c.d][c.f][c.e] = e;
                    e = (c.xa & 3840) >> 8;
                    c.h[c.d][c.f][c.e - 1] = e;
                    break;
                case 2:
                    c.i = c.i + 1;
                    c.e = c.a[2];
                    c.f = c.a[3];
                    c.d = c.a[0] >>> 2;
                    c.h[c.d][c.f][c.e] = c.j[c.b];
                    c.a[2] = (c.a[2] + c.m[c.b]) % 16;
                    break;
                case 3:
                    c.i = c.i + 1;
                    c.e = c.a[2] & 14;
                    c.f = c.a[3];
                    c.d = c.a[0] >>> 2;
                    c.h[c.d][c.f][c.e] = c.j[c.b];
                    c.h[c.d][c.f][c.e + 1] = c.m[c.b]
                }
                c.b = c.b + 1;
                break;
            case 2:
                c.i = c.i + 2;
                c.o = v;
                switch (c.q[c.b]) {
                case 0:
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] + c.j[c.b];
                    if (c.c < 16) {
                        c.h[c.d][c.f][c.e] =
                            c.c;
                        k = c.c !== 0 ? k & 12 : (k | 2) & 14
                    } else {
                        c.c = c.c % 16;
                        c.h[c.d][c.f][c.e] = c.c;
                        k = c.c !== 0 ? k & 13 | 1 : k | 3
                    }
                    c.a[0] = k;
                    break;
                case 2:
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] - c.j[c.b];
                    if (c.c >= 0) {
                        c.h[c.d][c.f][c.e] = c.c;
                        k = c.c !== 0 ? k & 12 : (k | 2) & 14
                    } else {
                        c.c = 16 + c.c;
                        c.h[c.d][c.f][c.e] = c.c;
                        k = c.c !== 0 ? k & 13 | 1 : k | 3
                    }
                    c.a[0] = k
                }
                c.b = c.b + 1;
                break;
            case 3:
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 0:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] & c.j[c.b];
                    c.h[c.d][c.f][c.e] = c.c;
                    k = c.c !== 0 ? k & 13 : k | 2;
                    c.a[0] = k;
                    break;
                case 1:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] | c.j[c.b];
                    c.h[c.d][c.f][c.e] = c.c;
                    k = c.c !== 0 ? k & 13 : k | 2;
                    c.a[0] = k;
                    break;
                case 2:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] ^ c.j[c.b];
                    c.h[c.d][c.f][c.e] = c.c;
                    k = c.c !== 0 ? k & 13 : k | 2;
                    c.a[0] = k;
                    break;
                case 3:
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    k = c.a[0];
                    c.c = c.h[c.d][c.f][c.e] - c.j[c.b];
                    k = c.c >= 0 ? c.c !== 0 ? k & 12 : (k | 2) & 14 : k & 13 | 1;
                    c.a[0] = k
                }
                c.b = c.b + 1;
                break;
            case 5:
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 0:
                    c.i =
                        c.i + 2;
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    c.z = c.m[c.b];
                    c.w = c.j[c.b];
                    c.h[c.d][c.f][c.e] = c.h[c.d][c.z][c.w];
                    break;
                case 2:
                    c.i = c.i + 4;
                    c.d = c.a[0] >>> 2;
                    c.f = c.a[3];
                    c.e = c.a[2];
                    c.z = c.m[c.b];
                    c.w = c.j[c.b];
                    e = c.h[c.d][c.f][c.e];
                    c.h[c.d][c.f][c.e] = c.h[c.d][c.z][c.w];
                    c.h[c.d][c.z][c.w] = e
                }
                c.b = c.b + 1;
                break;
            case 6:
                c.i = c.i + 3;
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 0:
                    f = 0;
                    c.a[0] = c.a[0] | 2;
                    do {
                        c.d = c.a[0] >>> 2;
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.c = c.h[c.d][c.f][c.e] + c.h[c.d][c.z][c.w] + (c.a[0] & 1);
                        if (c.c < 16) {
                            c.h[c.d][c.f][c.e] =
                                c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 12 : c.a[0] & 14
                        } else {
                            c.c = c.c % 16;
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 13 | 1 : c.a[0] | 1
                        }
                        c.a[2] = (c.a[2] + 1) % 16;
                        c.a[1] = c.a[1] !== 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0);
                    break;
                case 1:
                    f = 0;
                    c.a[0] = c.a[0] | 2;
                    do {
                        c.d = c.a[0] >>> 2;
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.c = c.h[c.d][c.f][c.e] + c.h[c.d][c.z][c.w] + (c.a[0] & 1);
                        if (c.c < 10) {
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 12 : c.a[0] & 14
                        } else {
                            c.c = c.c % 10;
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 13 | 1 : c.a[0] | 1
                        }
                        c.a[2] = (c.a[2] + 1) % 16;
                        c.a[1] = c.a[1] !== 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0);
                    break;
                case 2:
                    f = 0;
                    c.a[0] = c.a[0] | 2;
                    do {
                        c.d = c.a[0] >>> 2;
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.c = c.h[c.d][c.f][c.e] - c.h[c.d][c.z][c.w] - (c.a[0] & 1);
                        if (c.c >= 0) {
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 12 : c.a[0] & 14
                        } else {
                            c.c = c.c + 16;
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 13 | 1 : c.a[0] | 1
                        }
                        c.a[2] = (c.a[2] + 1) % 16;
                        c.a[1] = c.a[1] !== 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0);
                    break;
                case 3:
                    f = 0;
                    c.a[0] = c.a[0] | 2;
                    do {
                        c.d = c.a[0] >>> 2;
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.c = c.h[c.d][c.f][c.e] - c.h[c.d][c.z][c.w] - (c.a[0] & 1);
                        if (c.c >= 0) {
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 12 : c.a[0] & 14
                        } else {
                            c.c = c.c + 10;
                            c.h[c.d][c.f][c.e] = c.c;
                            c.a[0] = c.c !== 0 ? c.a[0] & 13 | 1 : c.a[0] | 1
                        }
                        c.a[2] = (c.a[2] + 1) % 16;
                        c.a[1] = c.a[1] > 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0)
                }
                c.b = c.b + 1;
                break;
            case 7:
                c.o = v;
                k = c.q[c.b];
                switch (k) {
                case 0:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    f = 0;
                    do {
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = (c.a[2] + c.j[c.b]) % 16;
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.h[c.d][c.f][c.e] = c.h[c.d][c.z][c.w];
                        c.a[2] = c.a[2] === 15 ? 0 : c.a[2] + 1;
                        c.a[1] =
                            c.a[1] > 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0);
                    break;
                case 1:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    f = 0;
                    do {
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = (c.a[2] + c.j[c.b]) % 16;
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        c.h[c.d][c.f][c.e] = c.h[c.d][c.z][c.w];
                        c.a[2] = c.a[2] > 0 ? c.a[2] - 1 : 15;
                        c.a[1] = c.a[1] > 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0);
                    break;
                case 2:
                    c.i = c.i + 4;
                    c.d = c.a[0] >>> 2;
                    f = 0;
                    do {
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.e;
                        e = c.h[c.d][c.f][c.e];
                        c.h[c.d][c.f][c.e] = c.h[c.d][c.z][c.w];
                        c.h[c.d][c.z][c.w] = e;
                        c.a[2] = c.a[2] === 15 ? 0 : c.a[2] + 1;
                        c.a[1] = c.a[1] > 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] >
                        0);
                    break;
                case 3:
                    c.i = c.i + 2;
                    c.d = c.a[0] >>> 2;
                    f = 0;
                    c.a[0] = c.a[0] | 2;
                    do {
                        f = f + 1;
                        c.f = c.a[3];
                        c.e = c.a[2];
                        c.z = c.m[c.b];
                        c.w = c.a[2];
                        e = c.a[0] & 1;
                        c.c = c.h[c.d][c.f][c.e] - c.h[c.d][c.z][c.w] - e;
                        c.a[0] = c.c >= 0 ? c.c !== 0 ? c.a[0] & 12 : c.a[0] & 14 : c.c !== 0 ? c.a[0] & 13 | 1 : c.a[0] | 1;
                        c.a[2] = c.a[2] === 15 ? 0 : c.a[2] + 1;
                        c.a[1] = c.a[1] > 0 ? c.a[1] - 1 : 15
                    } while (c.a[1] > 0)
                }
                c.b = c.b + 1;
                break;
            case 8:
                c.i = c.i + 1;
                c.o = v;
                c.d = c.a[0] >>> 2;
                c.f = c.a[3];
                c.e = c.a[2];
                c.p = c.q[c.b];
                e = c.j[c.b];
                k = c.a[0];
                for (f = c.h[c.d][c.f][c.e]; e >= 0;) {
                    k = f % 2 !== 0 ? k | 1 : k & 14;
                    f = f >>> 1;
                    e = e - 1
                }
                c.a[c.p] = f;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 9:
                c.i = c.i + 1;
                c.o = v;
                c.d = c.a[0] >>> 2;
                c.f = c.a[3];
                c.e = c.a[2];
                c.p = c.q[c.b];
                e = c.j[c.b];
                k = c.a[0];
                for (c.c = c.h[c.d][c.f][c.e]; e >= 0;) {
                    c.c = c.c << 1;
                    if (c.c >= 16) {
                        c.c = c.c % 16;
                        k = k | 1
                    } else k = k & 14;
                    e = e - 1
                }
                c.a[c.p] = c.c;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 10:
                c.i = c.i + 1;
                c.o = v;
                c.k = ((c.a[9] & 7) << 4) + (c.a[8] & 14) >>> 1;
                c.k = c.k !== 0 ? c.k - 1 : 63;
                c.d = 0;
                c.f = 8 + (c.k >>> 3);
                c.e = c.k % 8 << 1;
                c.g = c.m[c.b];
                if (c.g !== 30) {
                    if (c.g !== 48) {
                        if (c.g === 16) {
                            P(c, 16);
                            P(c, 17)
                        }
                        c.h[c.d][c.f][c.e] = c.a[c.g];
                        c.h[c.d][c.f][c.e +
                            1
                        ] = c.a[c.g + 1]
                    }
                } else {
                    c.v = c.a[27];
                    if (N(c.v) === v) {
                        if (c.v < 14) {
                            if ((c.a[24] & 1) !== 0) {
                                c.B = c.a[29];
                                c.A = c.a[28];
                                c.a[30] = c.F[c.v][c.B][c.A] % 16;
                                c.h[c.d][c.f][c.e] = c.a[30];
                                c.a[31] = c.F[c.v][c.B][c.A] >>> 4;
                                c.h[c.d][c.f][c.e + 1] = c.a[31]
                            }
                        } else if ((c.a[24] & 2) !== 0) {
                            c.B = c.a[29];
                            c.A = c.a[28];
                            c.a[30] = c.F[c.v][c.B][c.A] % 16;
                            c.h[c.d][c.f][c.e] = c.a[30];
                            c.a[31] = c.F[c.v][c.B][c.A] >>> 4;
                            c.h[c.d][c.f][c.e + 1] = c.a[31]
                        }
                        O(c)
                    }
                }
                c.a[8] = c.k * 2 % 16;
                c.a[9] = c.k >>> 3;
                c.b = c.b + 1;
                break;
            case 11:
                c.i = c.i + 1;
                c.k = ((c.a[9] & 7) << 4) + (c.a[8] & 14) >> 1;
                c.d = 0;
                c.f =
                    8 + (c.k >>> 3);
                c.e = c.k % 8 << 1;
                c.g = c.m[c.b];
                c.o = c.g !== 6 ? v : s;
                if (c.g !== 30) {
                    c.a[c.g] = c.h[c.d][c.f][c.e];
                    c.a[c.g + 1] = c.h[c.d][c.f][(c.e + 1) % 16];
                    switch (c.g) {
                    case 8:
                        c.a[c.g] = c.h[c.d][c.f][c.e] & 14;
                        c.a[c.g + 1] = c.h[c.d][c.f][(c.e + 1) % 16] & 7;
                        break;
                    case 44:
                        I(c);
                        break;
                    case 10:
                        H(c);
                        break;
                    case 42:
                        J(c);
                        K(c);
                        break;
                    case 22:
                        if ((c.a[22] & 2) !== 0) c.Y = s;
                        break;
                    case 24:
                        if ((c.a[c.g] & 1) !== 0) c.L = 255;
                        break;
                    case 58:
                        (c.a[58] & 1) !== 0 && Q(c)
                    }
                } else {
                    c.v = c.a[27];
                    if (N(c.v) === v) {
                        if (c.v < 14) {
                            if ((c.a[24] & 1) !== 0) {
                                c.B = c.a[29];
                                c.A = c.a[28];
                                c.a[30] = c.h[c.d][c.f][c.e];
                                c.a[31] = c.h[c.d][c.f][(c.e + 1) % 16];
                                c.F[c.v][c.B][c.A] = c.a[31] * 16 + c.a[30]
                            }
                        } else if ((c.a[24] & 2) !== 0) {
                            c.B = c.a[29];
                            c.A = c.a[28];
                            c.a[30] = c.h[c.d][c.f][c.e];
                            c.a[31] = c.h[c.d][c.f][(c.e + 1) % 16];
                            c.F[c.v][c.B][c.A] = c.a[31] * 16 + c.a[30]
                        }
                        O(c)
                    }
                }
                c.k = (c.k + 1) % 64;
                c.a[9] = c.k >>> 3;
                c.a[8] = (c.k << 1) % 16;
                c.b = c.b + 1;
                break;
            case 12:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                e = c.j[c.b];
                k = c.a[0];
                for (f = c.g !== 0 ? c.a[c.g] : 0; e >= 0;) {
                    k = f % 2 !== 0 ? k | 1 : k & 14;
                    f = f >>> 1;
                    e = e - 1
                }
                c.a[c.p] = f;
                c.a[0] = k;
                c.o = c.p !== 7 ? v : s;
                c.b = c.b + 1;
                break;
            case 13:
                c.i = c.i + 1;
                c.o =
                    v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                e = c.j[c.b];
                k = c.a[0];
                for (c.c = c.g !== 0 ? c.a[c.g] : 0; e >= 0;) {
                    c.c = c.c << 1;
                    if (c.c >= 16) {
                        c.c = c.c % 16;
                        k = k | 1
                    } else k = k & 14;
                    e = e - 1
                }
                c.a[c.p] = c.c;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 14:
                c.i = c.i + 1;
                c.g = c.m[c.b];
                c.o = c.g !== 7 ? v : s;
                c.e = c.a[2];
                c.f = c.a[3];
                c.d = c.a[0] >>> 2;
                c.a[c.g] = c.h[c.d][c.f][c.e];
                switch (c.g) {
                case 8:
                    c.a[c.g] = c.h[c.d][c.f][c.e] & 14;
                    break;
                case 9:
                    c.a[c.g] = c.h[c.d][c.f][c.e] & 7;
                    break;
                case 44:
                    I(c);
                    break;
                case 10:
                    H(c);
                    break;
                case 42:
                    J(c);
                    break;
                case 43:
                    K(c);
                    break;
                case 22:
                    if ((c.a[22] &
                        2) === 0) c.Y = s;
                    break;
                case 24:
                    if ((c.a[c.g] & 1) !== 0) c.L = 255;
                    break;
                case 58:
                    (c.a[58] & 1) !== 0 && Q(c)
                }
                c.b = c.b + 1;
                break;
            case 15:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                (c.g === 16 || c.g === 17) && P(c, c.g);
                c.d = c.a[0] >>> 2;
                c.f = c.a[3];
                c.e = c.a[2];
                c.h[c.d][c.f][c.e] = c.a[c.g];
                c.b = c.b + 1;
                break;
            case 16:
                c.i = c.i + 1;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] + c.j[c.b] : c.j[c.b];
                k = c.a[0];
                if (c.c < 16) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c % 16;
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 13 | 1 : k | 3
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 17:
                c.i = c.i +
                    1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] + e : e;
                k = c.a[0];
                if (c.c < 16) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c % 16;
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 13 | 1 : k | 3
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 18:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] - c.j[c.b] : 0 - c.j[c.b];
                k = c.a[0];
                if (c.c >= 0) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c + 16;
                    c.a[c.p] = c.c;
                    k = k & 13 | 1
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 19:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] - e : 0 - e;
                k = c.a[0];
                if (c.c >= 0) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c + 16;
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 13 | 1 : k | 3
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 20:
                c.i = c.i + 1;
                c.g = c.m[c.b];
                c.o = c.g !== 7 ? v : s;
                c.a[c.g] = c.j[c.b];
                switch (c.g) {
                case 8:
                    c.a[c.g] = c.j[c.b] & 14;
                    break;
                case 9:
                    c.a[c.g] = c.j[c.b] & 7;
                    break;
                case 44:
                    I(c);
                    break;
                case 10:
                    H(c);
                    break;
                case 42:
                    J(c);
                    break;
                case 43:
                    K(c);
                    break;
                case 22:
                    if ((c.a[22] & 2) === 0) c.Y = s;
                    break;
                case 24:
                    if ((c.a[c.g] &
                        1) !== 0) {
                        c.L = 255;
                        c.a[47] = c.a[47] & 14
                    }
                    break;
                case 58:
                    (c.a[58] & 1) !== 0 && Q(c)
                }
                c.b = c.b + 1;
                break;
            case 21:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                k = c.a[0];
                c.c = c.g !== 0 ? c.a[c.g] + e + (k & 1) : e + (k & 1);
                if (c.c < 16) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c % 16;
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 13 | 1 : k | 3
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 22:
                c.i = c.i + 1;
                c.g = c.q[c.b] << 1;
                c.o = c.g !== 6 ? v : s;
                c.a[c.g] = c.j[c.b];
                c.a[c.g + 1] = c.m[c.b];
                switch (c.g) {
                case 8:
                    c.a[c.g] = c.j[c.b] & 14;
                    c.a[c.g + 1] = c.j[c.b] &
                        7;
                    break;
                case 44:
                    I(c);
                    break;
                case 10:
                    H(c);
                    break;
                case 42:
                    J(c);
                    K(c);
                    break;
                case 22:
                    if ((c.a[22] & 2) === 0) c.Y = s;
                    break;
                case 24:
                    if ((c.a[c.g] & 1) !== 0) c.L = 255;
                    break;
                case 58:
                    (c.a[58] & 1) !== 0 && Q(c)
                }
                c.b = c.b + 1;
                break;
            case 23:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                k = c.a[0] >>> 0;
                c.c = c.g !== 0 ? c.a[c.g] - e - (k & 1) : 0 - e - (k & 1);
                if (c.c >= 0) {
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 12 : (k | 2) & 14
                } else {
                    c.c = c.c + 16;
                    c.a[c.p] = c.c;
                    k = c.c !== 0 ? k & 13 | 1 : k | 3
                }
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 24:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] & c.j[c.b] : 0;
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k | 2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 25:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] & e : 0;
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k | 2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 26:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] | c.j[c.b] : c.j[c.b];
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k | 2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 27:
                c.i =
                    c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] | e : e;
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k | 2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 28:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] ^ c.j[c.b] : 0 ^ c.j[c.b];
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k | 2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 29:
                c.i = c.i + 1;
                c.o = v;
                c.g = c.j[c.b];
                e = c.g !== 0 ? c.a[c.g] : 0;
                c.g = c.m[c.b];
                c.p = c.q[c.b];
                c.c = c.g !== 0 ? c.a[c.g] ^ e : 0 ^ e;
                k = c.a[0];
                c.a[c.p] = c.c;
                k = c.c !== 0 ? k & 13 : k |
                    2;
                c.o = c.p !== 7 ? v : s;
                c.a[0] = k;
                c.b = c.b + 1;
                break;
            case 30:
                c.i = c.i + 1;
                c.o = v;
                c.d = c.a[0] >>> 2;
                c.f = c.a[3];
                c.e = c.a[2] & 14;
                c.g = c.m[c.b];
                c.o = c.g !== 6 ? v : s;
                if (c.g !== 30) {
                    c.a[c.g] = c.h[c.d][c.f][c.e];
                    c.a[c.g + 1] = c.h[c.d][c.f][(c.e + 1) % 16];
                    switch (c.g) {
                    case 8:
                        c.a[c.g] = c.h[c.d][c.f][c.e] & 14;
                        c.a[c.g + 1] = c.h[c.d][c.f][(c.e + 1) % 16] & 7;
                        break;
                    case 44:
                        I(c);
                        break;
                    case 10:
                        H(c);
                        break;
                    case 42:
                        J(c);
                        K(c);
                        break;
                    case 22:
                        if ((c.a[22] & 2) === 0) c.Y = s;
                        break;
                    case 24:
                        if ((c.a[c.g] & 1) !== 0) c.L = 255;
                        break;
                    case 58:
                        (c.a[58] & 1) !== 0 && Q(c)
                    }
                } else {
                    c.v = c.a[27];
                    if (N(c.v) ===
                        v) {
                        if (c.v < 14) {
                            if ((c.a[24] & 1) !== 0) {
                                c.B = c.a[29];
                                c.A = c.a[28];
                                c.a[30] = c.h[c.d][c.f][c.e];
                                c.a[31] = c.h[c.d][c.f][(c.e + 1) % 16];
                                c.F[c.v][c.B][c.A] = c.a[31] * 16 + c.a[30]
                            }
                        } else if ((c.a[24] & 2) !== 0) {
                            c.B = c.a[29];
                            c.A = c.a[28];
                            c.a[30] = c.h[c.d][c.f][c.e];
                            c.a[31] = c.h[c.d][c.f][(c.e + 1) % 16];
                            c.F[c.v][c.B][c.A] = (c.a[31] << 4) + c.a[30]
                        }
                        O(c)
                    }
                }
                c.b = c.b + 1;
                break;
            case 31:
                c.i = c.i + 1;
                c.o = v;
                c.d = c.a[0] >>> 2;
                c.f = c.a[3];
                c.e = c.a[2] & 14;
                c.g = c.m[c.b];
                if (c.g !== 30) {
                    if (c.g !== 48) {
                        if (c.g === 16) {
                            P(c, 16);
                            P(c, 17)
                        }
                        c.h[c.d][c.f][c.e] = c.a[c.g];
                        c.h[c.d][c.f][c.e +
                            1
                        ] = c.a[c.g + 1]
                    }
                } else {
                    c.v = c.a[27];
                    if (N(c.v) === v) {
                        if (c.v < 14) {
                            if ((c.a[24] & 1) !== 0) {
                                c.B = c.a[29];
                                c.A = c.a[28];
                                c.a[30] = c.F[c.v][c.B][c.A] % 16;
                                c.h[c.d][c.f][c.e] = c.a[30];
                                c.a[31] = c.F[c.v][c.B][c.A] >>> 4;
                                c.h[c.d][c.f][c.e + 1] = c.a[31]
                            }
                        } else if ((c.a[24] & 2) !== 0) {
                            c.B = c.a[29];
                            c.A = c.a[28];
                            c.a[30] = c.F[c.v][c.B][c.A] % 16;
                            c.h[c.d][c.f][c.e] = c.a[30];
                            c.a[31] = c.F[c.v][c.B][c.A] >>> 4;
                            c.h[c.d][c.f][c.e + 1] = c.a[31]
                        }
                        O(c)
                    }
                }
                c.b = c.b + 1;
                break;
            case 32:
            case 33:
            case 34:
            case 35:
                c.i = c.i + 1;
                c.o = v;
                c.d = c.a[0] >>> 2;
                c.f = c.q[c.b];
                c.e = c.m[c.b];
                c.h[c.d][c.f][c.e] =
                    c.j[c.b];
                c.b = c.b + 1;
                break;
            case 36:
            case 37:
            case 38:
            case 39:
                c.b = c.b + 1;
                c.o = v;
                c.k = ((c.a[9] & 7) << 4) + (c.a[8] & 14) >>> 1;
                c.k = c.k !== 0 ? c.k - 1 : 63;
                c.d = 0;
                c.f = 8 + (c.k >>> 3);
                c.e = c.k % 8 << 1;
                c.h[c.d][c.f][c.e] = c.b & 15;
                c.h[c.d][c.f][c.e + 1] = (c.b & 240) >> 4;
                c.k = c.k - 1;
                c.d = 0;
                c.f = 8 + (c.k >>> 3);
                c.e = c.k % 8 << 1;
                c.h[c.d][c.f][c.e] = (c.b & 3840) >> 8;
                c.h[c.d][c.f][c.e + 1] = (c.b & 61440) >> 12;
                c.b = (c.b >>> 12 << 12) + c.j[c.b - 1];
                if (c.k << 1 > 15) {
                    c.a[9] = c.k >>> 3;
                    c.a[8] = (c.k << 1) % 16
                } else {
                    c.a[9] = 0;
                    c.a[8] = c.k << 1
                }
                break;
            case 40:
            case 41:
            case 42:
            case 43:
                k = c.b;
                c.i = c.i + 2;
                c.b =
                    c.b + 1;
                c.o = v;
                c.k = ((c.a[9] & 7) << 4) + (c.a[8] & 14) >>> 1;
                c.k = c.k !== 0 ? c.k - 1 : 63;
                c.d = 0;
                c.f = 8 + (c.k >>> 3);
                c.e = c.k % 8 << 1;
                c.h[c.d][c.f][c.e] = c.b & 15;
                c.h[c.d][c.f][c.e + 1] = (c.b & 240) >> 4;
                c.k = c.k - 1;
                c.d = 0;
                c.f = 8 + (c.k >>> 3);
                c.e = c.k % 8 * 2;
                c.h[c.d][c.f][c.e] = (c.b & 3840) >> 8;
                c.h[c.d][c.f][c.e + 1] = (c.b & 61440) >> 12;
                if (c.k * 2 > 15) {
                    c.a[9] = c.k >>> 3;
                    c.a[8] = (c.k << 1) % 16
                } else {
                    c.a[9] = 0;
                    c.a[8] = c.k << 1
                }
                c.b = c.j[k];
                break;
            case 44:
            case 45:
            case 46:
            case 47:
                c.i = c.i + 1;
                c.o = v;
                c.b = (c.a[0] & 2) !== 0 ? ((c.b >>> 0) / 4096 >>> 0) * 4096 + c.j[c.b] : c.b + 1;
                break;
            case 48:
            case 49:
            case 50:
            case 51:
                c.i =
                    c.i + 1;
                c.o = v;
                c.b = (c.a[0] & 2) === 0 ? ((c.b >>> 0) / 4096 >>> 0) * 4096 + c.j[c.b] : c.b + 1;
                break;
            case 52:
            case 53:
            case 54:
            case 55:
                c.i = c.i + 1;
                c.o = v;
                c.b = (c.a[0] & 1) !== 0 ? ((c.b >>> 0) / 4096 >>> 0) * 4096 + c.j[c.b] : c.b + 1;
                break;
            case 56:
            case 57:
            case 58:
            case 59:
                c.i = c.i + 1;
                c.o = v;
                c.b = (c.a[0] & 1) === 0 ? ((c.b >>> 0) / 4096 >>> 0) * 4096 + c.j[c.b] : c.b + 1;
                break;
            case 60:
            case 61:
            case 62:
            case 63:
                k = c.b;
                c.i = c.i + 1;
                if (c.o === s) c.b = c.a[7] * 4096 + c.j[k];
                c.o = v;
                c.b = ((c.b >>> 0) / 4096 >>> 0) * 4096 + c.j[k]
            }
            if (c.ua && (c.a[10] & 2) !== 0) {
                L(c);
                c.b = c.Qa;
                c.ua = v;
                c.a[40] = c.a[40] & 14
            }
            if (c.fa &&
                c.va) {
                L(c);
                c.b = c.Ra;
                c.fa = v;
                c.a[40] = c.a[40] & 13
            }
            if (c.ga && c.wa) {
                L(c);
                c.b = c.Sa;
                c.ga = v;
                c.a[40] = c.a[40] & 11
            }
            if (c.Ja) {
                L(c);
                c.b = c.Oa;
                c.Ja = v;
                c.a[40] = c.a[40] & 7
            }
            if (c.sa) {
                L(c);
                c.b = c.Na;
                c.sa = v;
                c.a[41] = c.a[41] & 14
            }
            if (c.Ka) {
                L(c);
                c.b = c.Pa;
                c.Ka = v;
                c.a[41] = c.a[41] & 13
            }(c.a[22] & 2) === 0 && (c.h[2][8][10] = 15)
        }
        o = (new Date - g.Wa) * 1E3;
        g.Wa = new Date;
        g.C[0].eb(o);
        g.C[1].eb(o);
        if (g.C[0].da && g.C[0].X) {
            if ((g.a[42] & 4) !== 0) {
                g.fa = s;
                g.a[40] = g.a[40] | 2
            }
            g.C[0].reset();
            g.C[0].da = v
        }
        if (g.C[1].da && g.C[1].X) {
            if ((g.a[43] & 4) !== 0) {
                g.ga = s;
                g.a[40] = g.a[40] |
                    4
            }
            g.C[1].reset();
            g.C[1].da = v
        }
        if (h.J.n.Y === s) {
            h.J.n.Y = v;
            g = h.Ca;
            o = [];
            var e = 0,
                j, l, q, n = [],
                i, c = g.La,
                k = g.Ma;
            g.context.fillStyle = "#ffffff";
            g.context.fillRect(c, k, 384, 160);
            g.context.fillStyle = "#000000";
            for (f = 14; f <= 15; f++)
                for (j = 0; j < 16; j++)
                    for (l = 0; l < 16; l++) {
                        o[e] = g.J.n.F[f][j][l];
                        e++
                    }
            j = k + 6;
            for (k = 0; k < g.mb; k++) {
                k % 12 === 0 && (j = j + 2);
                if (o[k] !== 0) {
                    l = o[k];
                    for (e = 0; e < 8; e++)
                        if ((l >> 7 - e & 1) !== 0) {
                            f = k % 12 * 16;
                            f = f + e * 2;
                            f = f + c;
                            g.context.fillRect(f, j, 2, 2)
                        }
                }
            }
            c = g.La + g.lb;
            k = g.Ma;
            for (q = 0; q < 96; q++)
                if ((o[372 + (q / 8 >>> 0)] & 1 << 7 - q % 8) !== 0) switch (q) {
                case 2:
                    n = [252, 180, 180, 180, 180, 148, 252];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 0;
                                    f = f + c;
                                    f = f + (i - 48);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 8:
                    n = [192, 32, 64, 128, 224];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 16;
                                    f = f + c;
                                    f = f + (i - 56);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 9:
                    n = [9, 13, 11, 9, 9];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 16;
                                    f = f + c;
                                    f = f + (i - 56);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 10:
                    n = [96, 80, 80, 80, 96];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 32;
                                    f = f + c;
                                    f = f + (i - 64);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 11:
                    n = [7, 4, 7, 4, 7];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 32;
                                    f = f + c;
                                    f = f + (i - 64);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 15:
                    n = [40, 40, 56, 40, 40];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 48;
                                    f = f + c;
                                    f = f + (i - 72);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 16:
                    n = [2, 2, 2, 1, 1];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 48;
                                    f = f + c;
                                    f = f + (i - 72);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [128, 128, 128, 0, 0];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 64;
                                    f = f + c;
                                    f = f + (i - 80);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 17:
                    n = [56, 40, 56, 32, 32];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 64;
                                    f = f + c;
                                    f = f + (i - 80);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 21:
                    n = [1, 1, 1, 1, 1];
                    for (e =
                        0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 64;
                                    f = f + c;
                                    f = f + (i - 80);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [192, 0, 192, 0, 0];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 80;
                                    f = f + c;
                                    f = f + (i - 88);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 22:
                    n = [16, 16, 16, 16, 16];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 80;
                                    f = f + c;
                                    f = f + (i - 88);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 23:
                    n = [5, 5, 2, 5, 5];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !==
                            0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 80;
                                    f = f + c;
                                    f = f + (i - 88);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 27:
                    n = [36, 40, 48, 40, 36];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 96;
                                    f = f + c;
                                    f = f + (i - 96);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 31:
                    n = [255, 66, 36, 24, 36, 66, 255];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 112;
                                    f = f + c;
                                    f = f + (i - 104);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 35:
                    n = [31, 20, 23, 22, 21, 16, 31];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !==
                            0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 128;
                                    f = f + c;
                                    f = f + (i - 112);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [192, 64, 64, 192, 192, 64, 192];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 144;
                                    f = f + c;
                                    f = f + (i - 120);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 41:
                    g.context.fillStyle = "#000000";
                    n = [6, 8, 4, 2, 12];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 144;
                                    f = f + c;
                                    f = f + (i - 120);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 42:
                    n = [96, 128, 128, 128, 96];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 160;
                                    f = f + c;
                                    f = f + (i - 128);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 43:
                    n = [8, 8, 8, 8, 8];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 160;
                                    f = f + c;
                                    f = f + (i - 128);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 44:
                    n = [2, 3, 2, 2, 2];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 160;
                                    f = f + c;
                                    f = f + (i - 128);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [64, 64, 192, 64, 64];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !==
                            0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 176;
                                    f = f + c;
                                    f = f + (i - 136);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 45:
                    n = [14, 16, 22, 18, 14];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 176;
                                    f = f + c;
                                    f = f + (i - 136);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 46:
                    n = [224, 128, 224, 128, 224];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 0;
                                    f = f + c;
                                    f = f + (i + 48);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 47:
                    n = [9, 13, 11, 9, 9];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !==
                            0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 0;
                                    f = f + c;
                                    f = f + (i + 48);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 48:
                    n = [56, 64, 88, 72, 56];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 16;
                                    f = f + c;
                                    f = f + (i + 40);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 53:
                    n = [1, 1, 1, 1, 1];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 16;
                                    f = f + c;
                                    f = f + (i + 40);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [64, 64, 192, 64, 64];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i =
                                0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 32;
                                    f = f + c;
                                    f = f + (i + 32);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 54:
                    n = [28, 16, 28, 16, 28];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 32;
                                    f = f + c;
                                    f = f + (i + 32);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 55:
                    n = [1, 1, 0, 1, 1];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 32;
                                    f = f + c;
                                    f = f + (i + 32);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [64, 64, 128, 64, 64];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >>
                                    7 - i & 1) !== 0) {
                                    f = 48;
                                    f = f + c;
                                    f = f + (i + 24);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 56:
                    n = [12, 18, 18, 18, 12];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 48;
                                    f = f + c;
                                    f = f + (i + 24);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 57:
                    n = [96, 128, 128, 128, 96];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 64;
                                    f = f + c;
                                    f = f + (i + 16);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 58:
                    n = [14, 4, 4, 4, 4];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >>
                                    7 - i & 1) !== 0) {
                                    f = 64;
                                    f = f + c;
                                    f = f + (i + 16);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 68:
                    n = [127, 83, 93, 83, 93, 67, 127];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 80;
                                    f = f + c;
                                    f = f + (i + 8);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 76:
                    n = [48, 40, 40, 40, 48];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 96;
                                    f = f + c;
                                    f = f + i;
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 77:
                    n = [3, 2, 3, 2, 3];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >>
                                    7 - i & 1) !== 0) {
                                    f = 96;
                                    f = f + c;
                                    f = f + i;
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [128, 0, 128, 0, 128];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 112;
                                    f = f + c;
                                    f = f + i;
                                    f = f - 8;
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 78:
                    n = [28, 32, 44, 36, 28];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 112;
                                    f = f + c;
                                    f = f + (i - 8);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 79:
                    n = [1, 1, 1, 1, 1];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 112;
                                    f = f + c;
                                    f = f + (i - 8);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [192, 64, 192, 128, 64];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 128;
                                    f = f + c;
                                    f = f + (i - 16);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 80:
                    n = [8, 20, 20, 28, 20];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 128;
                                    f = f + c;
                                    f = f + (i - 16);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 81:
                    n = [1, 1, 1, 1, 1];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 128;
                                    f = f + c;
                                    f = f + (i -
                                        16);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [128, 64, 64, 64, 128];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 144;
                                    f = f + c;
                                    f = f + (i - 24);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 87:
                    n = [4, 8, 30, 8, 4];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 144;
                                    f = f + c;
                                    f = f + (i - 24);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 89:
                    n = [32, 112, 168, 32, 0];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 160;
                                    f = f + c;
                                    f = f + (i - 32);
                                    j = k;
                                    g.context.fillRect(f,
                                        j + e, 1, 1)
                                }
                        }
                    break;
                case 91:
                    n = [0, 0, 2, 1, 0];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 160;
                                    f = f + c;
                                    f = f + (i - 32);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    n = [0, 128, 160, 192, 128];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 176;
                                    f = f + c;
                                    f = f + (i - 40);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                    break;
                case 93:
                    n = [4, 2, 15, 2, 4];
                    for (e = 0; e < n.length; e++)
                        if (n[e] !== 0) {
                            l = n[e];
                            for (i = 0; i < 8; i++)
                                if ((l >> 7 - i & 1) !== 0) {
                                    f = 176;
                                    f = f + c;
                                    f = f + (i - 40);
                                    j = k;
                                    g.context.fillRect(f, j + e, 1, 1)
                                }
                        }
                }
        }
        h.Da =
            setTimeout(d, 0)
    };
    if (F) try {
        this.G.Ib()
    } catch (q) {
        this.u(q.message)
    }
    d()
};
TI30.prototype.resetEmulator = function () {
    var d = (new Date).getTime();
    return d > this.Ea + 3E3 ? (this.Ea = d, this.deleteKeyHistory(), clearTimeout(this.Da), this.P(), s) : v
};
TI30.prototype.hideCalculator = function () {
    var d = v,
        h;
    if (h = document.getElementById("calculatorDiv")) h.style.visibility = "hidden", Z = d = s;
    return d
};
TI30.prototype.showCalculator = function () {
    var d = v,
        h;
    if (h = document.getElementById("calculatorDiv")) h.style.visibility = "visible", d = s, Z = v;
    return d
};
TI30.prototype.resize = function (d) {
    var h = v;
    if ("string" === typeof d)
        if (Z === v) {
            switch (d.toLowerCase()) {
            case "small":
                d = 0.75;
                break;
            case "medium":
                d = 1;
                break;
            case "large":
                d = 1.5;
                break;
            case "extra large":
                d = 2;
                break;
            default:
                return this.u("The calculator scale must be specified by its string name representation (small, medium, large or extra large)."), h
            }
            0 < d && (this.scale = d, d = document.getElementById("calculatorDiv"), d.style.width = this.Hb * this.scale + "px", d.style.height = this.Gb * this.scale + "px", h = s)
        } else this.u("The calculator cannot be resized when it is hidden.");
        else this.u("The calculator scale must be specified by its string name representation (small, medium, large or extra large).");
    return h
};
TI30.prototype.enableAllKeys = function () {
    var d = s;
    try {
        this.G.enableAllKeys()
    } catch (h) {
        d = v, this.u(h.message)
    }
    return d
};
TI30.prototype.disableAllKeys = function () {
    var d = s;
    try {
        this.G.disableAllKeys()
    } catch (h) {
        d = v, this.u(h.message)
    }
    return d
};
TI30.prototype.disableKeys = function (d) {
    var h = s;
    if ("string" === typeof d) try {
        this.G.disableKeys(d)
    } catch (l) {
        h = v, this.u(l.message)
    } else h = v, this.u("Invalid filename. Valid characters are alpha-numeric, underscore, dash, dot and space.");
    return h
};
TI30.prototype.getKeyHistory = function () {
    return this.G.yb()
};
TI30.prototype.hasBeenUsed = function () {
    var d = v;
    try {
        this.G !== p && (d = this.G.Z, this.G.Z = v)
    } catch (h) {
        d = v, this.u("Cannot determine if the calculator has been used.")
    }
    return d
};
TI30.prototype.deleteKeyHistory = function () {
    var d = v;
    this.G !== p && (this.G.Xa(), d = s);
    return d
};