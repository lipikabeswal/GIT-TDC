/*
 jQuery Text Highlighter
 Copyright (C) 2012 by mirz

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
*/
(function (d, n, l, m) {
    var h, j;

    function k(a, e) {
        this.context = a;
        this.$context = d(a);
        this.options = d.extend({}, d[h].defaults, e);
        this.init()
    }
    j = 1;
    h = "textHighlighter";
    k.prototype = {
        init: function () {
            this.$context.addClass(this.options.contextClass);
            this.bindEvents()
        },
        destroy: function () {
            this.unbindEvents();
            this.$context.removeClass(this.options.contextClass);
            this.$context.removeData(h)
        },
        bindEvents: function () {
            this.$context.bind("mouseup", {
                self: this
            }, this.highlightHandler)
        },
        unbindEvents: function () {
            this.$context.unbind("mouseup",
                this.highlightHandler)
        },
        highlightHandler: function (a) {
            a.data.self.doHighlight()
        },
        doHighlight: function () {
            var a = this.getCurrentRange();
            if (a && !a.collapsed) {
                var e = a.toString();
                if (!0 == this.options.onBeforeHighlight(a)) {
                    var f = d.textHighlighter.createWrapper(this.options);
                    this.options.onAfterHighlight(this.normalizeHighlights(this.highlightRange(a, f)), e)
                }
                this.removeAllRanges()
            }
        },
        getCurrentRange: function () {
            var a = this.getCurrentSelection(),
                d;
            0 < a.rangeCount && (d = a.getRangeAt(0));
            return d
        },
        removeAllRanges: function () {
            this.getCurrentSelection().removeAllRanges()
        },
        getCurrentSelection: function () {
            var a = this.getCurrentWindow(),
                e;
            a.getSelection ? e = a.getSelection() : d("iframe").length ? d("iframe", top.document).each(function () {
                if (this.contentWindow === a) return e = rangy.getIframeSelection(this), !1
            }) : e = rangy.getSelection();
            return e
        },
        getCurrentWindow: function () {
            var a = this.getCurrentDocument();
            return a.defaultView ? a.defaultView : a.parentWindow
        },
        getCurrentDocument: function () {
            return this.context.ownerDocument ? this.context.ownerDocument : this.context
        },
        highlightRange: function (a,
            e) {
            if (!a.collapsed) {
                var f = "SCRIPT STYLE SELECT BUTTON OBJECT APPLET".split(" "),
                    c = a.startContainer,
                    b = a.endContainer,
                    i = a.commonAncestorContainer,
                    g = !0;
                if (0 == a.endOffset) {
                    for (; !b.previousSibling && b.parentNode != i;) b = b.parentNode;
                    b = b.previousSibling
                } else 3 == b.nodeType ? a.endOffset < b.nodeValue.length && b.splitText(a.endOffset) : 0 < a.endOffset && (b = b.childNodes.item(a.endOffset - 1));
                3 == c.nodeType ? a.startOffset == c.nodeValue.length ? g = !1 : 0 < a.startOffset && (c = c.splitText(a.startOffset), b == c.previousSibling && (b = c)) :
                    c = a.startOffset < c.childNodes.length ? c.childNodes.item(a.startOffset) : c.nextSibling;
                var i = !1,
                    h = [];
                do {
                    if (g && 3 == c.nodeType) {
                        if (/\S/.test(c.nodeValue)) {
                            var g = e.clone(!0).get(0),
                                j = c.parentNode;
                            if (d.contains(this.context, j) || j === this.context) g = d(c).wrap(g).parent().get(0), h.push(g)
                        }
                        g = !1
                    }
                    if (c == b && (!b.hasChildNodes() || !g)) i = !0; - 1 != d.inArray(c.tagName, f) && (g = !1);
                    g && c.hasChildNodes() ? c = c.firstChild : null != c.nextSibling ? (c = c.nextSibling, g = !0) : (c = c.parentNode, g = !1)
                } while (!i);
                return h
            }
        },
        normalizeHighlights: function (a) {
            this.flattenNestedHighlights(a);
            this.mergeSiblingHighlights(a);
            return d.map(a, function (a) {
                return "undefined" != typeof a.parentElement ? null != a.parentElement ? a : null : null != a.parentNode ? a : null
            })
        },
        flattenNestedHighlights: function (a) {
            var e = this;
            d.each(a, function (f) {
                var c = d(this),
                    b = c.parent(),
                    i = b.prev(),
                    g = b.next();
                e.isHighlight(b) && (b.css("background-color") != c.css("background-color") ? (e.isHighlight(i) && (i.css("background-color") != b.css("background-color") && i.css("background-color") == c.css("background-color")) && c.insertAfter(i), e.isHighlight(g) &&
                    (g.css("background-color") != b.css("background-color") && g.css("background-color") == c.css("background-color")) && c.insertBefore(g), b.is(":empty") && b.remove()) : (c = l.createTextNode(b.text()), b.empty(), b.append(c), d(a[f]).remove()))
            })
        },
        mergeSiblingHighlights: function (a) {
            function e(a, b) {
                return b && b.nodeType == j && d(a).css("background-color") == d(b).css("background-color") && d(b).hasClass(f.options.highlightedClass) ? !0 : !1
            }
            var f = this;
            d.each(a, function () {
                var a = this.previousSibling,
                    b = this.nextSibling;
                if (e(this,
                    a)) {
                    var f = d(a).text() + d(this).text();
                    d(this).text(f);
                    d(a).remove()
                }
                e(this, b) && (f = d(this).text() + d(b).text(), d(this).text(f), d(b).remove())
            })
        },
        setColor: function (a) {
            this.options.color = a
        },
        getColor: function () {
            return this.options.color
        },
        removeHighlights: function (a) {
            var e = this;
            this.getAllHighlights(a !== m ? a : this.context, !0).each(function () {
                if (!0 == e.options.onRemoveHighlight(this)) {
                    var a = d(this).contents().unwrap().get(0),
                        c = a.previousSibling,
                        b = a.nextSibling;
                    c && 3 == c.nodeType && (a.nodeValue = c.nodeValue + a.nodeValue,
                        c.parentNode.removeChild(c));
                    b && 3 == b.nodeType && (a.nodeValue += b.nodeValue, b.parentNode.removeChild(b))
                }
            })
        },
        getAllHighlights: function (a, e) {
            var f = "." + this.options.highlightedClass,
                f = d(a).find(f);
            !0 == e && d(a).hasClass(this.options.highlightedClass) && (f = f.add(a));
            return f
        },
        isHighlight: function (a) {
            return a.hasClass(this.options.highlightedClass)
        }
    };
    d.fn.getHighlighter = function () {
        return this.data(h)
    };
    d.fn[h] = function (a) {
        return this.each(function () {
            d.data(this, h) || d.data(this, h, new k(this, a))
        })
    };
    d.textHighlighter = {
        createWrapper: function (a) {
            return d("<span></span>").css("backgroundColor", a.color).addClass(a.highlightedClass)
        },
        defaults: {
            color: "#ffff7b",
            highlightedClass: "highlighted",
            contextClass: "highlighter-context",
            onRemoveHighlight: function () {
                return !0
            },
            onBeforeHighlight: function () {
                return !0
            },
            onAfterHighlight: function () {}
        }
    }
})(jQuery, window, document);