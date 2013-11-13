/*
 * This file contains the unicode characters which need to be replaced
 * in content items in DHTML runtime to fix the issues with characters
 * getting clipped in DHTML textfields for superscript and subscript
 * sections, fractions and other custom characters used in the
 * OASmathv3 font.
 *
 * The list of characters was assembled based on the PDF document
 * OASmathv3LookUpBook.pdf (Fri Jun 15 09:52:20 2007)
 *
 *
 */

var unicode = {};

// SUPERSCRIPT GLYPHS
unicode.superscript = {
    // superscript lower case letters
    0xF814: "a",
    0xF815: "b",
    0xF816: "c",
    0xF817: "d",
    0xF818: "e",
    0xF819: "f",
    0xF81A: "g",
    0xF81B: "h",
    0xF81C: "i",
    0xF81D: "j",
    0xF81E: "k",
    0xF81F: "l",
    0xF820: "m",
    0xF821: "n",
    0xF822: "o",
    0xF823: "p",
    0xF824: "q",
    0xF825: "r",
    0xF826: "s",
    0xF827: "t",
    0xF828: "u",
    0xF829: "v",
    0xF82A: "w",
    0xF82B: "x",
    0xF82C: "y",
    0xF82D: "z",

    // superscript upper case letters
    0xF7F8: "A",
    0xF7F9: "B",
    0xF7FA: "C",
    0xF7FB: "D",
    0xF7FC: "E",
    0xF7FD: "F",
    0xF7FE: "G",
    0xF7FF: "H",
    0xF800: "I",
    0xF801: "J",
    0xF802: "K",
    0xF803: "L",
    0xF804: "M",
    0xF805: "N",
    0xF806: "O",
    0xF807: "P",
    0xF808: "Q",
    0xF809: "R",
    0xF80A: "S",
    0xF80B: "T",
    0xF80C: "U",
    0xF80D: "V",
    0xF80E: "W",
    0xF80F: "X",
    0xF810: "Y",
    0xF811: "Z",

    // Other glyphs used
    0x207C: "=",
    0x207D: "(",
    0x207E: ")",
    0x207F: "n"
}


unicode.digitsSuperscript = {
    // superscript digits
    0x2070: "0",
    0x00B9: "1",
    0x00B2: "2",
    0x00B3: "3",
    0x2074: "4",
    0x2075: "5",
    0x2076: "6",
    0x2077: "7",
    0x2078: "8",
    0x2079: "9"
}

// SUBSCRIPT GLYPHS
unicode.subscript = {
    // subscript lower case letters
    0xF8B6: "a",
    0xF8B7: "b",
    0xF8B8: "c",
    0xF8B9: "d",
    0xF8BA: "e",
    0xF8BB: "f",
    0xF8BC: "g",
    0xF8BD: "h",
    0xF8BE: "i",
    0xF8BF: "j",
    0xF8C0: "k",
    0xF8C1: "l",
    0xF8C2: "m",
    0xF8C3: "n",
    0xF8C4: "o",
    0xF8C5: "p",
    0xF8C6: "q",
    0xF8C7: "r",
    0xF8C8: "s",
    0xF8C9: "t",
    0xF8CA: "u",
    0xF8CB: "v",
    0xF8CC: "w",
    0xF8CD: "x",
    0xF8CE: "y",
    0xF8CF: "z",

    // subscript upper case letters
    0xF89A: "A",
    0xF89B: "B",
    0xF89C: "C",
    0xF89D: "D",
    0xF89E: "E",
    0xF89F: "F",
    0xF8A0: "G",
    0xF8A1: "H",
    0xF8A2: "I",
    0xF8A3: "J",
    0xF8A4: "K",
    0xF8A5: "L",
    0xF8A6: "M",
    0xF8A7: "N",
    0xF8A8: "O",
    0xF8A9: "P",
    0xF8AA: "Q",
    0xF8AB: "R",
    0xF8AC: "S",
    0xF8AD: "T",
    0xF8AE: "U",
    0xF8AF: "V",
    0xF8B0: "W",
    0xF8B1: "X",
    0xF8B2: "Y",
    0xF8B3: "Z",

    // Other glyphs used
    0x208A: "+",
    0x208B: "-",
    0x208C: "=",
    0x208D: "(",
    0x208E: ")",
    0xF8B4: ",",
    0xF8B5: ".",
    0xF8EA: "-"
}


unicode.digitsSubscript = {
    // subscript digits
    0x2080: "0",
    0x2081: "1",
    0x2082: "2",
    0x2083: "3",
    0x2084: "4",
    0x2085: "5",
    0x2086: "6",
    0x2087: "7",
    0x2088: "8",
    0x2089: "9"
}


unicode.numerator = {

        0xF82E: '&#xF82E;',         // Numerator_A
        0xF82F: '&#xF82F;',         // Numerator_B
        0xF830: '&#xF830;',         // Numerator_C
        0xF831: '&#xF831;',         // Numerator_D
        0xF832: '&#xF832;',         // Numerator_E
        0xF833: '&#xF833;',         // Numerator_F
        0xF834: '&#xF834;',         // Numerator_G
        0xF835: '&#xF835;',         // Numerator_H
        0xF836: '&#xF836;',         // Numerator_I
        0xF837: '&#xF837;',         // Numerator_J
        0xF838: '&#xF838;',         // Numerator_K
        0xF839: '&#xF839;',         // Numerator_L
        0xF83A: '&#xF83A;',         // Numerator_M
        0xF83B: '&#xF83B;',         // Numerator_N
        0xF83C: '&#xF83C;',         // Numerator_O
        0xF83D: '&#xF83D;',         // Numerator_P
        0xF83E: '&#xF83E;',         // Numerator_Q
        0xF83F: '&#xF83F;',         // Numerator_R
        0xF840: '&#xF840;',         // Numerator_S
        0xF841: '&#xF841;',         // Numerator_T
        0xF842: '&#xF842;',         // Numerator_U
        0xF843: '&#xF843;',         // Numerator_V
        0xF844: '&#xF844;',         // Numerator_W
        0xF845: '&#xF845;',         // Numerator_X
        0xF846: '&#xF846;',         // Numerator_Y
        0xF847: '&#xF847;',         // Numerator_Z

        0xF848: '&#xF848;',         // Numerator_Comma
        0xF849: '&#xF849;',         // Numerator_Period

        0xF84A: '&#xF84A;',         // Numerator_a
        0xF84B: '&#xF84B;',         // Numerator_b
        0xF84C: '&#xF84C;',         // Numerator_c
        0xF84D: '&#xF84D;',         // Numerator_d
        0xF84E: '&#xF84E;',         // Numerator_e
        0xF84F: '&#xF84F;',         // Numerator_f
        0xF850: '&#xF850;',         // Numerator_g
        0xF851: '&#xF851;',         // Numerator_h
        0xF852: '&#xF852;',         // Numerator_i
        0xF853: '&#xF853;',         // Numerator_j
        0xF854: '&#xF854;',         // Numerator_k
        0xF855: '&#xF855;',         // Numerator_l
        0xF856: '&#xF856;',         // Numerator_m
        0xF857: '&#xF857;',         // Numerator_n
        0xF858: '&#xF858;',         // Numerator_o
        0xF859: '&#xF859;',         // Numerator_p
        0xF85A: '&#xF85A;',         // Numerator_q
        0xF85B: '&#xF85B;',         // Numerator_r
        0xF85C: '&#xF85C;',         // Numerator_s
        0xF85D: '&#xF85D;',         // Numerator_t
        0xF85E: '&#xF85E;',         // Numerator_u
        0xF85F: '&#xF85F;',         // Numerator_v
        0xF860: '&#xF860;',         // Numerator_w
        0xF861: '&#xF861;',         // Numerator_x
        0xF862: '&#xF862;',         // Numerator_y
        0xF863: '&#xF863;',         // Numerator_z

        0xF8E0: '&#xF8E0;',         // Numerator_0
        0x215F: '&#x215F;',         // Numerator_1
        0xF8E2: '&#xF8E2;',         // Numerator_2
        0xF8E3: '&#xF8E3;',         // Numerator_3
        0xF8E4: '&#xF8E4;',         // Numerator_4
        0xF8E5: '&#xF8E5;',         // Numerator_5
        0xF8E6: '&#xF8E6;',         // Numerator_6
        0xF8E7: '&#xF8E7;',         // Numerator_7
        0xF8E8: '&#xF8E8;',         // Numerator_8
        0xF8E9: '&#xF8E9;',         // Numerator_9

        0xF8E1: '&#xF8E1;'         // Horizontal line of Numerator

}


unicode.denominator = {

        0xF864: '&#xF864;',         // Denominator_A
        0xF865: '&#xF865;',         // Denominator_B
        0xF866: '&#xF866;',         // Denominator_C
        0xF867: '&#xF867;',         // Denominator_D
        0xF868: '&#xF868;',         // Denominator_E
        0xF869: '&#xF869;',         // Denominator_F
        0xF86A: '&#xF86A;',         // Denominator_G
        0xF86B: '&#xF86B;',         // Denominator_H
        0xF86C: '&#xF86C;',         // Denominator_I
        0xF86D: '&#xF86D;',         // Denominator_J
        0xF86E: '&#xF86E;',         // Denominator_K
        0xF86F: '&#xF86F;',         // Denominator_L
        0xF870: '&#xF870;',         // Denominator_M
        0xF871: '&#xF871;',         // Denominator_N
        0xF872: '&#xF872;',         // Denominator_O
        0xF873: '&#xF873;',         // Denominator_P
        0xF874: '&#xF874;',         // Denominator_Q
        0xF875: '&#xF875;',         // Denominator_R
        0xF876: '&#xF876;',         // Denominator_S
        0xF877: '&#xF877;',         // Denominator_T
        0xF878: '&#xF878;',         // Denominator_U
        0xF879: '&#xF879;',         // Denominator_V
        0xF87A: '&#xF87A;',         // Denominator_W
        0xF87B: '&#xF87B;',         // Denominator_X
        0xF87C: '&#xF87C;',         // Denominator_Y
        0xF87D: '&#xF87D;',         // Denominator_Z

        0xF848: '&#xF87E;',         // Denominator_Comma
        0xF849: '&#xF87F;',         // Denominator_Period

        0xF880: '&#xF880;',         // Denominator_a
        0xF881: '&#xF881;',         // Denominator_b
        0xF882: '&#xF882;',         // Denominator_c
        0xF883: '&#xF883;',         // Denominator_d
        0xF884: '&#xF884;',         // Denominator_e
        0xF885: '&#xF885;',         // Denominator_f
        0xF886: '&#xF886;',         // Denominator_g
        0xF887: '&#xF887;',         // Denominator_h
        0xF888: '&#xF888;',         // Denominator_i
        0xF889: '&#xF889;',         // Denominator_j
        0xF88A: '&#xF88A;',         // Denominator_k
        0xF88B: '&#xF88B;',         // Denominator_l
        0xF88C: '&#xF88C;',         // Denominator_m
        0xF88D: '&#xF88D;',         // Denominator_n
        0xF88E: '&#xF88E;',         // Denominator_o
        0xF88F: '&#xF88F;',         // Denominator_p
        0xF890: '&#xF890;',         // Denominator_q
        0xF891: '&#xF891;',         // Denominator_r
        0xF892: '&#xF892;',         // Denominator_s
        0xF893: '&#xF893;',         // Denominator_t
        0xF894: '&#xF894;',         // Denominator_u
        0xF895: '&#xF895;',         // Denominator_v
        0xF896: '&#xF896;',         // Denominator_w
        0xF897: '&#xF897;',         // Denominator_x
        0xF898: '&#xF898;',         // Denominator_y
        0xF899: '&#xF899;',         // Denominator_z

        0xF8D0: '&#xF8D0;',         // Denominator_0
        0xF8D1: '&#xF8D1;',         // Denominator_1
        0xF8D2: '&#xF8D2;',         // Denominator_2
        0xF8D3: '&#xF8D3;',         // Denominator_3
        0xF8D4: '&#xF8D4;',         // Denominator_4
        0xF8D5: '&#xF8D5;',         // Denominator_5
        0xF8D6: '&#xF8D6;',         // Denominator_6
        0xF8D7: '&#xF8D7;',         // Denominator_7
        0xF8D8: '&#xF8D8;',         // Denominator_8
        0xF8D9: '&#xF8D9;'          // Denominator_9

}



/* Single glyph fractions are replaced by a numerator_? and <sub>denominator</sub> */
unicode.singleGlyphFraction = {

    /*
    // fractions
    0x00BC: "&#x00BC;",         // fraction 1/4
    0x00BD: "&#x00BD;",         // fraction 1/2
    0x00BE: "&#x00BE;",         // fraction 3/4
    0x2153: "&#x2153;",         // fraction 1/3
    0x2154: "&#x2154;",         // fraction 2/3
    0x2155: "&#x2155;",         // fraction 1/5
    0x2156: "&#x2156;",         // fraction 2/5
    0x2157: "&#x2157;",         // fraction 3/5
    0x2159: "&#x2159;",         // fraction 1/6
    0x215A: "&#x215A;",         // fraction 5/6
    0x215B: "&#x215B;",         // fraction 1/8
    0x215C: "&#x215C;",         // fraction 3/8
    0x215D: "&#x215D;",         // fraction 5/8
    0x215E: "&#x215E;",         // fraction 7/8
    */

    // fractions / replaced by denominator_? with subscript
    0x00BC: { numerator: "&#x215F", denominator: "4" },  // fraction 1/4
    0x00BD: { numerator: "&#x215F", denominator: "2;" },  // fraction 1/2
    0x00BE: { numerator: "&#xF8E0", denominator: "4" },  // fraction 3/4
    0x2153: { numerator: "&#x215F", denominator: "3" },  // fraction 1/3
    0x2154: { numerator: "&#xF8E2", denominator: "3" },  // fraction 2/3
    0x2155: { numerator: "&#x215F", denominator: "5" },  // fraction 1/5
    0x2156: { numerator: "&#xF8E2", denominator: "5" },  // fraction 2/5
    0x2157: { numerator: "&#xF8E3", denominator: "5" },  // fraction 3/5
    0x2159: { numerator: "&#x215F", denominator: "6" },  // fraction 1/6
    0x215A: { numerator: "&#xF8E0", denominator: "6" },  // fraction 5/6
    0x215B: { numerator: "&#x215F", denominator: "8" },  // fraction 1/8
    0x215C: { numerator: "&#xF8E3", denominator: "8" },  // fraction 3/8
    0x215D: { numerator: "&#xF8E5", denominator: "8" },  // fraction 5/8
    0x215E: { numerator: "&#xF8E7", denominator: "8" }   // fraction 7/8

}


unicode.spaces = {
    0x2002: "&#x2002;",         // EN_Space
    0x2003: "&#x2003;",         // EM_Space
    0x2004: "&#x2004;",         // thrid_EM_Space
    0x2005: "&#x2005;",         // Fourth_EM_Space
    0x2006: "&#x2006;",         // Sixth_EM_Space
    0x2008: "&#x2008;",         // Punctuation_Space
    0x2009: "&#x2009;",         // Thin_Space
    0x200A: "&#x200A;"          // Hair_Space
}

/* Character class for Superscript_Root_Extender and Super_Overbar
 * characters. Both character glyphs extend beyond the top limit of the
 * character box and are not rendered correctly. Currently we do not
 * process the characters, these section has been adde in case we decide
 * to process these chars.
 */
unicode.superscriptRoot = {
	0xE004: "&#xE004;",		    // Superscript_Root_Extender
	0xE000: "&#xE000;" 		    // Super_Overbar
}
