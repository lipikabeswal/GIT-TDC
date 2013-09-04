        ��  ��                  �      �� ���    0 	        <html>
<head>
<title>Binding Test</title>
<script language="JavaScript">

// Register the callback for messages from the browser process.
app.setMessageCallback('binding_test', function(name, args) {
  document.getElementById('result').value = "Response: "+args[0];
});

// Send a message to the browser process.
function sendMessage() {
  var msg = document.getElementById("message").value;
  app.sendMessage('binding_test', [msg]);
}
</script>

</head>
<body>
<form>
Message: <input type="text" id="message" value="My Message">
<br/><input type="button" onclick="sendMessage();" value="Send Message">
<br/>You should see the reverse of your message below:
<br/><textarea rows="10" cols="40" id="result"></textarea>
</form>
</body>
</html>
   -      �� ���    0 	        <html>
<head>
<title>Dialog Test</title>
<script>
function show_alert() {
  alert("I am an alert box!");
}

function show_confirm() {
  var r = confirm("Press a button");
  var msg = r ? "You pressed OK!" : "You pressed Cancel!";
  document.getElementById('cm').innerText = msg;
}

function show_prompt() {
  var name = prompt("Please enter your name" ,"Harry Potter");
  if (name != null && name != "")
    document.getElementById('pm').innerText = "Hello " + name + "!";
}

window.onbeforeunload = function() {
  return 'This is an onbeforeunload message.';
}

function update_time() {
  document.getElementById('time').innerText = new Date().toLocaleString();
}

function setup() {
  update_time();
  setInterval(update_time, 1000);
}

function show_file_dialog(element, test) {
  var message = 'DialogTest.' + test;
  var target = document.getElementById(element);

  // Register for the callback from OnFileDialogDismissed in dialog_test.cpp.
  app.setMessageCallback(message, function(msg, paths) {
    target.innerText = paths.join();
    app.removeMessageCallback(message);
  });

  // This will result in a call to OnProcessMessageReceived in dialog_test.cpp.
  app.sendMessage(message);
}

window.addEventListener('load', setup, false);
</script>
</head>
<body>
<form>
Click a button to show the associated dialog type.
<br/><input type="button" onclick="show_alert();" value="Show Alert">
<br/><input type="button" onclick="show_confirm();" value="Show Confirm"> <span id="cm"></span>
<br/><input type="button" onclick="show_prompt();" value="Show Prompt"> <span id="pm"></span>
<br/>input type="file": <input type="file" name="pic" accept="text/*,.js,.css,image/*">
<br/><input type="button" onclick="show_file_dialog('fo', 'FileOpen');" value="Show File Open"> <span id="fo"></span>
<br/><input type="button" onclick="show_file_dialog('fom', 'FileOpenMultiple');" value="Show File Open Multiple"> <span id="fom"></span>
<br/><input type="button" onclick="show_file_dialog('fs', 'FileSave');" value="Show File Save"> <span id="fs"></span>
<p id="time"></p>
</form>
</body>
</html>
   �      �� ���    0 	        <html>
<body>
<p id="instructions">Select some portion of the below page content and click the "Describe Selection" button. The selected region will then be described below.</p>
<p id="p1">This is p1</p>
<p id="p2">This is p2</p>
<p id="p3">This is p3</p>
<p id="p4">This is p4</p>
<form>
<input type="button" id="button" value="Describe Selection">
<p id="description">The description will appear here.</p>
</form>
</body>
</html>
      �� ���    0 	        <html>
<body>
<script language="JavaScript">
var val = window.localStorage.getItem('val');
function addLine() {
  if(val == null)
    val = '<br/>One Line.';
  else
    val += '<br/>Another Line.';
  window.localStorage.setItem('val', val);
  document.getElementById('out').innerHTML = val;
}
</script>
Click the "Add Line" button to add a line or the "Clear" button to clear.<br/>
This data will persist across sessions if a cache path was specified.<br/>
<input type="button" value="Add Line" onClick="addLine();"/>
<input type="button" value="Clear" onClick="window.localStorage.removeItem('val'); window.location.reload();"/>
<div id="out"></div>
<script language="JavaScript">
if(val != null)
  document.getElementById('out').innerHTML = val;
</script>
</body>
</html>
  �M      �� ���    0 	        �PNG

   IHDR     _   �D#   sRGB ���   gAMA  ���a   	pHYs  �  ��+  M<IDATx^�}�T��3e�lo,K�]�Л�  �Xb�[�Q�)&��i71�7W�\c5Uc:1&5Ƃ�\����e������|g��3�,;�;� ��;�egϜ���{�k�!A�H�@\G���	���������A��w{|�I�'���Wŵ=��a���aN��X����u�#�E����j�K@���������f�qڴ(/�@jj�Q5l�L4;���Ǯ^�=}��hE�ӎ�.c@;�,�g�ï�_�eAz�y�L�q"?7��V�̂�u~T��Dc�4�5m���QA�"��&v��r�R����#�)�I�
f̞�ɓ'c�G��u0!x�o�b�/���AS�D��"?����N��*�n���'����^G�Iv`L6X�=Ʌ��L*�u0�՚����4--�x�������Ԇ��I�j���&�N|�.�r.� EEEHOO?"�z�F�
�pp	/o�`�~Z;��e��Lw������<N�|X��2�n����fG��* "�!`��b ��j�$\��'a^�%��1&SX�%F�0� ś��v��ա��_]�O7ތ>G,h��{������=�����0}���H���΄\��]A���G�Kĕ�F�� pD&f� ;͇qc,���y8����E�/m-���bo���4(��t��& �@�L����ې���f��I~,��*KOp,�iC˯�!ꕵ�A�c��m���.�_R3���h��k���m��ޓ��͛wT�7z�G&��n(yc�}=~�w��W�����ً�t/f5!?��yp�9����ڑ��"�=U]$=
ݝN4�[�ƣ�ɉ��V8��h`!��H�#+��V�����*��p����;�C�;���痠�Ӡ8�|��p��('��I��?^q��p���?�����#E���Bw�-]>t����R\���sJ�(�ۧ�%EV��J1�� ��+a�"���ރ����ņ�U">��bKJ���J2s*�K!�D�L�/�[�� �����b&M�$I���O��9�9m`��3�����0nlA�O>�n6�T�����Q]/"�p m>����# "%�~̟X��]J�4n�8���( IO����I!�ut���Cɥ77��7&	'�%%;*��6ѥ�[�䥾F�j.X��u�R��;�R�E�D�3!��E�K\�nX� &g����F��ӎj=߰�d�ƞ�܈���)}�]��K�1n�xL.-E^�X�fg@Z�X�A߶�O�꒫)��$G��v��d�����ٔ%�G����lR�ڕ�N�����	X#�Y�/\�[��	9��;�s��FY����#�9���SbM�\�]�o�SJ�Kf��i�X�`1,\��oȺ�����5a��X��/��͗"��Wo;j��t��OKW���������T�T�潵^)�����K(�*����~$(f0i��ƽ�r*%+uT�6�y��ц��z̝Є�寣lJ�M��)���"�F<e�x�������<��K�W��v��e� Jg�:u�,�>�Y�ILt���I����%W������=:��1�	9��>�R>#r$���v8��1���N�U�{4oQt8�N7�R�?s�4���t,�������M��"���۸N�݊e3��6m�K'�v�$���� �^�Jx�;�Q�-��筇�)��	;���`w=��! ��lV���e��6��G<E���Tt4�^]��g�2�e�J�n����{�@B���7�qI�{�@j��|Z]�����|X{'#4(�P�پ?$ԑ�#�Q6�K��@ ��J��(���CP��I
HΞդ8z&��Þ�c��f1�%�}ԙ$�$!Ɓw4�!���V�6$:�)ёP�����t�m࡛{Gk�־Ӆ�^mC��/Ɋ9~T�=�vW��J<wtF` ��&�$�:�����i�a|�`�qk/^�?C�J i�C�8��B}u������ZkF�N����@�pJ�,oS֥9z�lb2NPb�DYz}�&����ْ��;M�wDZn���$F�4z��uI1��*R9���HJKJ�Cڑ$�1��ܥ���B|�����E�!�P:��:�3�x�9��v�rJ�XP_rD�t荊
&�h��HșP��:{�wv�2ӥ��I&L26�Ds&۔�xΔ,Y�D�r��r���H�D[F�'!���[]����o��� 0�x�~kH:{<J��×��Oq%�JRP4��P�F�6����	ndtO⩱� ��H�Gd\K��G3��	�O�n�Ix1�Q�d;S���&��4bm��ћT{�͓�h�Gg$*_yi�r�|z&�7���3�Q_�K���F��Q17���|�p��hvL�X+��uL(L�h&�D�{����a�~?�k+��mt�\IK��&��%�t7��y�8gYZ �˄X�𘾏���W�1��	����>��{�~��I���W\��n}�����)���N�8�SK��w&����"���v����C"ݛ����}���2��~
��w7�O��.s*�րo�?� )Ӧ���g��)��ۮ��'W�i�e���KWta��q��i��L#� �w6@û������o�vu4�dKKF�+	Y�n��W6^�B�g/@��Ԅ~�D �wl��a-��_��E2`�|���1Y@����2���g i�)pe���+�u.�V9 I͒�Y IL�@\~���?u�\��S�{<�봡M��q�v�4'	��_��)g=�5h�����k����V�?�F�C_m#,O[6Ɵe���Gv���9H��RIQ�pH�ZL����J���-2?���I��3S\��i�8��S���r��A9q��������Ш^�u���|+�<�=E�4����Jr'R�=鞞�O���?�"�UL҄����H�}VvJ��8��I|%Z�5^�,cc��x��^���� $�&���H�z5z'\�̤������@�}o�WE���i>U??�s�F�Y�$�l�������ƌ�$\sf�U0t��f40!�@�O��'��*PW΄�h=�^F�_�d6QU����a��}�/�$�˯@��+c�3&�����A�$8"��L�V��{\�(����6����U�M�B#�A���f��6I� ���[P;q�@�z�N~������0	�����oOu��7TX�9vw�ꎛ��v�AEs"Iu������mNT�cW�R4�$r�"#Kr�$ِ�l��-A[6cɤ7����#G�[�
) ,|�%cZ3~����9
ua�#��V^��Į:���
\ϒl�b*�'�g�����kZ �5��ߨ�-�q�X��| n�J�XX\rPr�9�:�&�lq�gұ�� �p��l�X6�;�l��C���o�Dnw'$��1U�9a������~4M���}wP�T&D������i���LN����''��\w�7`#��W�o���WL@[����r��dK�|!��}�]3%e׶P��k�u�d����!v�0X���~��_��Q\��sෆ8C��Q=�
D6t���yʂ�Ly����i�*�^Sr'�E޻i=���JM���@Eq6�+���ە7r<�I��k��"9�>q�tˉ<!�-iC���˂�-6�G�JLn�x���4fN����we'u�,��e<Y�q��`�R.�3A��F!:���?�)�7�� I�dr�a��5�z�xb�����S����C,�?|�-��s���K��m��6f.8~�9S`B硗�����L0DK��e-���e�ؖ�J#7b����E��7���.�F^-���'���2L�yN�8p�ч���b�C�7U�S
K#�X�"���o����bC)�@��[&6��6�,^ �m_����{�qI�n!w-��=��+B�D������ڍ  m����V}ٳ};�^kp)v9�"�҉�d��k[�/@�w�%���៞sbO�G��a0���s˼��xc�����3[�����99���.�WV�5�8�F*�0?w���H��t��{������c��fV�c@����=� ��F� hԀ"�M��HŜhk�_D��NZ��i3��/,??'����q������M��ğ��G
����>и=�.6����ϜP�|��m�^c��Dh�Ze,��`ʆ�8�A�ɬY��z�G�(��f%	$�H�Սh��b�yː=�r��p��Wp��ރ�;�A�[UpH�}k���_'�`�3� ���~P�3��$�X���/���G(�h �(^�ʊ2{k'�BPiܽc�N�T�\
u.$�	I�H"�u
Q�C�������X��c�L� �@Hn��No��C������;/n��$��6�r6L����ן.%)�B��u2��<�5g����/!�Igb�o���ƫ�#�|�
%,m*�����^��_��%�5+#L�A׶��?�Q����o��m}ɩ��I_%I��$y���^��C~���bz�x��剬y�˙�������7�X�T	��K�NCAN.�Dl�����,d��n�1ͭ{��.))�=RNҚ���}�� :
�v��������s�ry~���T4G"�C�PQ�^�T	�d�э��J?D�q�y��jCVA)�E���	�R�����ȥ�Ċ!E�H��ػ���HU�����/t�+�"7R�� 	�&d�����$��L���%��1//�i2fij����(�s`aY���B[�(Se�{e�}~��J���΋im[
Dd8y~6�D�����kH��fB�_U=��$r�^(>���oq���+��cU��� �sc� ���uM�ַ�JjW�;k)�杂��fc�ĉ��C�!�c�=~vO�}����V��s����`�G ���~��W�h&kM33ˇ��#�h��V�d���UL9։�����+��H(�t���ܙ�@B����:N<������;hv�S��V)\F ��Ó�_�Hi5M��v'8w�q��qB�)������,V�5#�'��V�t��}�<�l��*N�v�ܒ���H�31�����б�:�5��8"G�z �(����+ ܎S��Sl=�b�N�0���������Ϥ"-�tΤ�7�4u������'�*�k�ds�y����|�r�H8d�H�mR-�b�Z��Sˆ\f��6����9>� Q�]�r�ĩ�����&�Q���{�e�ozKƢ'Lh:#xP�m��]t�O�Y`��<(�5��<��G%����cGd�(���"�履��.�4��\��X��yvW <(�$b|�[�=O���;���~P�	�([)ڴָ�+��Z%c��K�e�)�cO�tA�n�q�:���/h{
����8z���"eVh���1�8|qE/�ORZ?u �9�0ihoor(�t�ڿ���W7V��N��^��;��ԗ��W,|��!iء��P�ok�p@gG�a�����8�:J~�y��;�=��+�!1.`m���,����P��{�]Ͼ�tdf��y'Q�!�3q�}���Ί�3G��<�{��$�����k�q�DU҃,j<R��V�n�M{��HӀb6U����*�+���O���u1/n�f��PG��$�J���ҋ۱\��#�/H<�u܉�G�;1�ED�EL� tX��{��"p0N�gz]
�>Qԓ�&��TV���� �]r�d���НP̵��k����͎����Y��h8D9eӻj-��E!r�[q%S���l~�N�>YbV�5�-�v���t�x�"bO��A���{��	[{�-�W�Eݘ{/��R��zl��p=��A��� ������kA����h6���7\����,��D�!���c�}�yqӣ5ͷ�h&�7���ł��0i��˪PZ�Dٔɕ؛r��Nz�@��,�PS�3q�p~�&t��/�<=�έ�ϴ3-.�c�amSV��p`@�x���I��������l�ю���2�� kq��Lby���`�{~M�}���)����_H,Lӑ�7���u$��� ��Q݌�}�ø+݂��&��D���y�'M��a�>ֈb�:D��,e�����q���Sp8�Pc9�Գ�q�	�H�vM�LH)����'l�;���wȕ��7�}r%��4u�Q��h���7v�-��3�s�Cr&)I���LBʁ��"�c�zc���s�'�Z\�Eg���X}���zP�>s������d���b5�5��ίn����aP.J?���Ru�����BP�Q���M�H�W��9yE#�þG�.��#i���E��zQ;�M�;�S��:|��K�b0�K̤�~FQGM��Ԏ5�n��eW��3Z�����\�1eW�h�5w��N)����Ǟ�^U'ġDr%J�!�z����<���~�y�xufV"մ���D5@��]nŝ�v>1 0���ކPߙJ�!-��ў!������3��_� /�AE��E���]D���T���}(���"Ez�R�&Q���vkO����h<�b��p5:eN(�tw��J�o�����Y���M(��̽�"����+��l"����͜	�i�2���d��*�Q�Rqk���������D��<�R,�ߠo�鷝�gY��,���q��ݪI���+kM,�/���\��S΀���m �hPQ������Z��FڷoA� ��0�$���g:���@��P���� �y��04�u��1v8R�w�&�.]��.\�Q�;���s����z�����ɝ4B	���a��<u�*T�ޑ�����{S#L�N��r����}�\����U���E΄JY��:M-=ؿ�NT!�ށ���p=:6�| =]u�WM��
GU� ����`{����_��~
�o��;{ЕmG��9N�8@�W���oވ��_��˗a�������̇��&~$r'.������H�16�$0Q��2�ps��"%~�=���¬���#�nF�%x���h�9��b�/=(�@/&Ȭ��K[I�u�ʨ}����z,��e��u�SjE�>��z�2���ޮ6�,D_r�t%�MDgb��%bds]��;F�oM!�N!"C����s$�	�C���S�Ta� k1��т^�f�}��Ƌ��2=8T���C�˺��e!5E��
HǢ֥��+_��������ێ�?j�3{��%�<yv~�:���2L]u��Zr�	
D��ˣf0����"��	I�;d�F��Ⲏ���R�w�=W�6�R�]%������^�7_u�����nTɽ�?��Q�� ��}��论G��]x�Ӹ>�:��c��I}�yC[.��Dk�M6Eʒ5��@i��s!NYv�p���FX\��|���шyH%�j���C+�&g�8b��3Cn���LsV�t�y�"���q%ܫm�o^tE�[�I-J�
7��5}X�����S(�6��F
���I�x��ִ3�Za�n�$�Bw	�cyW�#��C���L�.��B�N%@��G��l��He"ޛ�	��m�Do�|.��-�����g��[_�Btez*�e�j>X)ި5g��)��Y�!�۶׍o �p�F?�����w�	7ʁT�tk`�^���냟6 ���T�_3��-;�A����_!�o� ���q�ǒ���o��%J���-���=�׵#� ���{�E�"��gpC�(b�ǟ�)��Q�Q�.*���]���;��m�y�^�������8ՙ�"Nj�c�a�x�.X�T��K���M'�b�QR�FlN�y�f��"�X,�Z����i8�֮���>\p��q�]!�S���Il�w,^��9�~��S��{��d����5�\�3�8�D���7՘�U��`�����?b�o'�]�6��[����r�A��q�/FEE���uA\��<����}����0φ[�����`�O6���/�E��B�wT��/�Z�zt��	8DW���[���8�&d� {ff��3*E��L�%W��������m�1)���o�˟(�K_���]�t!��=mL��%(*��
uv��+G�������Fgk���ĝ;����YR�<D+q��8�2b���Y�)���_��7��pD�oBY�+�C��72eI������S���U9\�⤸1+��S���+����eb�������ċ�E�N��̸��°������_E4��Ͽ�ť�����s_�@�tz?VU�8�<�r*2��рd�<�*��o߆���2�7s{v���P��*c���K���ʠ��Ց�߰�������ST=�IH̥�~�U&�ġ��%�EsUO�<���x���ʲ*�\y��E���~enٌo�lB��Iȑ�
0�_��O����T��v� 7L/�3��@��� �������s�jL32T�E�ci�e�LZ��ٽ��^a����8�ּ�`��H-�(�с�HQ�L��kwKˢ�l�nCg�ڶc����o4��J�%��Io��^z]��3�����((A裐���$P���N5�	�/ ��}��k/�o��Ex }
�����|�3��e�r��닄U�)~
�*𬷞��_"+����4���6��Ԅ!Me�����-�&[��ضȆ}�%����}��(�#:"K��hnQQ�n).^�,|�}-����J_���lʦ�{sƝ�~�3B��x���y��w�n
�\-��L��z�9w<�������x�?7��ѕg�[��S��~�+�!����]�&Lz�- tj���9kΙ�'o�t�Yr�)��7�T�B�
�{a_���=h� bv����ꚮ�-�j�{�!7	��7�Yq��Z�p�ޣ�/��k�K���_������,����t6�㦤Xw(�t��^�M�}����I�Ќ��Ϭ`;'%�x�=����x�j�g��Jjê%aY�>�g4�O�W�555�"2(�i�!�ɼS̡>[�<_#�ʙ��)hZ��=��7��31n}�"�P��=�2��1��/@nq��ړ���DS�2�)�`��.8���/NIEݝa���-I��qp�"㷨�-�����ո�5Ȑ+����y�®'����?#Ko�b�s��X�r)�q�rl�I2�-�t��8C�r!:����)����$YV��C�IQ�(�:5�v�MePT��a:�7 ��P7�鐜�@
؁4�Q�_��4�����LJރ��-��.��i���I�'�b��ܷ~�0]\����� ��K/9� R��t � ��BWz�(��zJ�0�k[$O�D�>��_jsTXB���h���%bap����/1>��Ϡx��$�ǈ���T��F�I��,���!޲��1�_"���C)c�����{oR�o�,&r��E�^t�i(��%8ᢋ�t�R�)Q�u��an�:��O�T�t�0ڶn���^��P�}?C�C�{Xw+���?��f��*��+���`��І1ƻ�&���"���-w���F���k��;��V�|������% ~w��r�R\rN J��5+ ��|���T$fE�C��O ��͜�W~g�i ���YcP�J��8���g��E�����-���]c�(��3�u���7�s��Ԉ�3w�Bf��8�i��1$+#3���aSh��F H�v	�AN����|v�9Wb�O#�ۣ�>j���,�;KOE�\�N@ˌ��Yq<^3ߞ�U|z��Cd:��/�t�J�:m�Q�R�B�e�6���S�a���&�J����:�mn	���s�I�t=n���8�Zh��]��{jWKu���a?��?h=!�Uę���~r ~�	���C!�؝�)5�7�}Y872�!-K$��D1����K��$l��
pW��xp���������̞�
Y�n���0�t�Xa�yDz��h���V^����[���uuC�r +����j�?es�H"$ɭf����0]а�e���9���cl���q&��-I�]��[�yd/���kw�kɼx�2��`p%?�L��|?ƍ	���6�i�]Oj厩m3L�XW�\�̺���2���_�v��y�s���T���4U�-�&��E|�h��j�/�V���xs!�FY�Ѣ���Ɯ�!RwB��U��.ð��[V��'������V��@96�V8����IR��>�]����v��?�v엄6����?����tL8�����_���:�}_���:�c�N��E�sb��96l�>��D��k��fb��/a"��]5�+�/_�����7W.�uV��"��	7�E�.�2��oL�+��g�~�����D���">��4�8F�=7�pͭ!�������Cؿ��e��hb?����G��z�ܽpV}M�ki�}��_Gg v&`%��]�qa�һ֩jF��E�2�1%+E8����(sN�ߖ�"nd�5�5+C=}�ۆY{Ֆ�Q+�.	>���CΆ��U*���r|�@���i@����}O���͟���`vO��ׁN��Iު��v�[u*p�z���f ê1�S����h��K����a?�%�S�(ڍ$O�-��[r04��T�]�@羰/x
�]�?Ȍ���(�0���D�Y�"F,8��\|&x:��26�Pj�L~nq���}l�][&	�u��N_""��S�ŧ'���f7�K������S�e��p�]E��L(	���~c/�3�|�kv��"���Ud�H1G�B���
QH+���SW���[0��/�V�$f\��~W�����7��ˋ1q���X{A�jabNN��Ɲ�����8�Q��Z��U�+AES�-�,�֟b����Q��h��i9�g�=wzZӟ�qۤ���0:}©w��gkO�s_�n�3���r�dI��k�xUJ׀�Q Ӛ�4���U8sŴ!ۢ��l&~��^<��$��iC^����J�	`��7��T����h�4N5]�@��pX��+G��R�����H�[�)�Xrc�S蟌ˡ;�Ϯ��T���Mʬ6|��Rȿ�f�H��r����+:qkC��/:=�����,���]�9+%���-tZ�o�zv�-�|�2IS�k8��}Y�{ꫪ��Hr�6*]�[W��G�J�rtTK❀^�	����U�5��d<yۮ\��4��vD���I�'��Eo��˝CN��b]��-�P尔2/L������ҸTb%�P0�t� �֕P+�!�$�f�ᰄ���f)*��G����{�o��@{�;��И���ka9��{fO�`�i�{�Sc����**���'�I��DL/�x&���g��N|�n\�d;�X��X��Z�׮؍+Oj�E�k�,N��_C���B"9��0e�)C�	$d_ǥ�Q)��a<ruw���"����4R�d�#YyK�~As �`���:ē5��DVNɩ����t�bb�̫B�_���P�,g�� g��݇�]���">�.�5��kj��C�xF�F��f*�[.�(���� %Yx�锋{��Ϟ̐���Ar�{J<Ce��9�& ��\!Cu��q��xo��I�Vy��ʠ��<�qZ���u�ע^�|~n���7�a��u*��� �p��Xb����0l͑0 P���<�y�w�<#��-�*K�|��X4���.�
�\d���eK�`�bu͜=Oi�W,*Ejv���m��"��BL"H\}�F�� q�TA$���ĖOP��t
;��)@��Q�D��9��q�ǡ�Y�8F4���I�I�)��O�D�c�N�M��;�+��`�Ĕt��!vݾ��AS�t>]���_o `���ڼ*�92j]ߣ�G|�e�j���*�9VR���ȁ�#I�7����+nf0�'Gr���X/���3��`E�d�'�Ϙ��?�1Ї��dؾETR?X�~�0�+�>7��8~n������>��� L������bD�C��Ǹ���"쫮t�F4��e$9n�i�R�D�lx1!��2���z�� ]�Ҽ�H��O��PZFK�w&��}�n�� ��̛A3� ���]�w��D7;;�SbL�����3g⅌�������r%}��G�Y0Z:�����I^82*L��������S���'O��$�8�"�P�a�:��z�������U%G&���R�BPa�I�/�g�o�����k*iu$�P�fݨ;�֋���9WBq��'Tc����K����TMfa_���-騸e��0��ĮM,�G��q-���:�M��I�$~���^ly��Q?�"�u�p�^vj�dEt�)�}O�� @M���n��AS�ĻR���mf�FB+ 	�������|'��JLh��d���d��a}�e��I�W�X�z��T�9�ժԀ&fg�+���i��o�;���~��e5A��{p��:�;�CR��Q; R�B^\̲��dpz��qs�9L��ƍM@��sEX<�@�C��r�u���lp)(>w�R̯~Ŋזּ��S��z�p)���>��K�%K*Hr���]��q{0�<S���@2��$WbrXc5���6����J��9��~�'	(�wĞ�s(m8Խa`��~��)()��H�1���Z,P�b�vlj2+�׻�-x�ź��Oī��y��2�hd�"Wb���v�R�!R��/ŋD�$U̧v"y��ȩ
����t�J�k��<�d%�0�[4�3�3?���갣�7E
�2��CNa^�Ҿ�Q����C}��hi}K)_)�+!uuyt�=	���Ɯ���B��֫��Ϥ`�T$Y��R���ݦ ��ӎ�N��\hk���>�:�~�L�N+_��ԌJ����䤊�()��$@,���� �j~yo��r�*)�jpCͥP�a�6M�vY%��͐tF��a��2�ӽ����J�#�O�d����7?����U`����� ��*�T^��\��E��-����j	�{K��X����Hj5<Ҙ�Y�5W����H� ���\g�K���[
I���If-�O�|��ktXdk�.X{�Y�xՎj�JX�����J�}$�0�d����+'����9q��6WnCq�����ϴ�P�V�d�Q)O ᥭ|���w�J���1�X<��/.�ȕDK��5t�ǔ���s����X,,�{�8NN��	E���_C�#��~0��pצ�{�	�x��t�XX�J\�'i@Ѻ�C�����b��e�5E�щh��
<�5��h����cɒ�}��b]S��v�`ى�w��Y2�g��!��ҍ�p�;Q�C��VYт�K�p%�e�'0�M�{��U(M�ȒL�<��vTK�td�����lPW��*U�L��NI�t`��*;~ɸ�����L+Ǣg₳������=��Ь,~�,ڈs�����\�'��G��щ��3�T��xf��)"�D��
g���_�	u��M�� �ڧ,f�<��?�r(�%5����Q��h|Å�Q6.Ii���R5����hoP�E����=V��@��:��֍%i�*R�L��?@�Q������&��D�1��9E��t�����zƟS��W�e'k��[ނm�[
HȕPW���9�$gL���8q���/�QW"�bA%��}q�0˿��*.Gde�+�J�]3?�j�ktw�XƋ떜5�lX�����>Z�l����ڥD�%����%�3���g�w�U�i� ��p&
Lhi�Bm�;Ī�u��/ᘕNOƘ�'��{����ߎ���
&l0�&,���k�tu��P�|�L
��e*���+��w��~�gb��!ȱ�&�
p[ښ3س9y=����ǲ�	(�q�Ek�@B���G����M!MP����F�Wr%ԕ�+�x�:�t�N�saq����d�x�rTc2i�d�^fPa�Ά���"��q8�j���9PNj�!�f�؏�2�=�D�8��	Y ��O�q��i����b���̰�2�ڈ��A1'�f_D��X˃*N���[8�X�	(�i���,����"
�Μ��3v���Qݗ�	ɲ�߾̐�TT�v/��ס��81�J�t���� ryD-�X>^�HH���j���'�s1����k��|V
/��f���T>�2�Z=b�470�y�;(ް�Z�(	$c��f(��wɲ��Vvi0e#E�
O4%���i��A���G�"�l��=�*]�gӘ�%��ǂ�U�ҏᬓ���9k�[�6$�_�ncS�q�2O� (�D4�*
S�#���\��(9������ip˺��� �8%�8 ���U.�?��Ի�h��ވ���\� ��^�!�D;���u���k!Zy(4�'F:@��~��3�m8a��Q���\Dڒ���?s�{�	Y������z�����br"5������7Ԯ����Tě���F=�\	��J��E�p��J̡�z��2Nd�sV��ŗ)qCon�V/\� 5�"��l��>xa�}!�����'i�o��{�ؘz���H�t�7�Pt��l��2�3�ZbBo����kK�go݈7�`�Q�0ӊ�\)��!j��ե���s�O�X����j�5ɚ7�y���f�������*�p���O݀=�UycЖ9&8N�ۄ��dI�[<>e��x��.Q\I6�( ��R'=<3a�_Je�%2P��*Ѷ���O�����`��Zŵ�n<�Q�J{|�P��o���ܕ��
�*qIY�B�����6�E�D�����5�Q,�ON�~QT��"}�,Y꾤��>�\-��Z�∆�w"~�շ+�����>i;9��~�ǃޘy��Cz��g�%I]����ғ�����+�>4~Q��U],r�]�rp���geEy�,�0b�}n�[�A�+���ANJ��t���9�9S*Qp�e���t1�>��W���%��(Zp��bq*�YS�ݽi��F�l�X�;Pۚ�B��D������x;9[�^��� �'��#͊%c$�Q^�ڜ������wJ	Z������f׸i<�|c�\�-���P��㗟�;��}��ox�ۉ�4q|�YU�Kz���*�lX���A��n��;~'%7Z����T %9	iii���@�CM;�ڪ��m��lEk�����lr%#I
Q���J6q��֍:�UbFd�&���Q�,14�`li4Yڍ[{��{C���.�!IoI�~ũ������9s��imm�2�U�':��O���)2I)�R����Q�e+��_~W�-�ϬeB��i�9/��>P(�.S6�([��/�t���fbN��4R�o]�YUf���H�BT���ΖSN��&K�%C�%�gG�%��vܡ����V�,r$�����5�J���rjِ�4A��uL�������<(�U��4(��{�ie�.����~���H$���{��[�.��=I}�O2�RpR�"L�8
�0��SC���$)�@{��m������wk�w�'�zJ$�K�D�h�$���s)�FQHSF~�ن��+{��+"?#�l�I�ޯ_����.���Hl[L`9�#wlۅ_��Ͽa�EHP�%:��Mq�p��Q��T��-�Yl�o3Z�B0���Zz9����N�t�&�@5Զ�	|�]�󧪢��|N`T8��/A�����`�4�z�aO�5�X���b	n����u]m��|I8y4�gT/7����֭[q`ݟ1��!d� iPɐu�"�$���{~J�)a���oXڍ�LE+�.��s�֘OK����J�5��I�:�a/�/����o:��ޭ?r�z�R�!����#��Թ6�p�?fE�n�{M[q�?�V�vnx����JEor�X�ܲ�9F|\gn���Q�4_/�r բ�x��?��������_�@��aӳX�-�� B��o�x��<̘Z��S��3�����յ�x��u�ݳ���p)�E�
��[����L������g�Osc��,L*䌃��Z/���C/�c�~1��X.��(���f��a��e��WNO�y?���[�[�X������z�y#'/U@��n�����2Mϝ��m����a�g2����ƍD�G-+3��Z��G�$'KΓl�SƓ�
X��`�#�BҞ�{:˱%�\���f���g��IC��Jk#�mh��\4g����W�p���UH���a�ci;�s�c��{���g��tv�A�j6OM���s.���;kkE|�R����^Lxg�Ao3��������n�5L��Yq"�L��ly2��ʑJ�����^���	���O<q��M�J�ړ��~�<L��	2s+i¹���� �ᓟ����_���6�4�Z��=^�: Q�x��Ͱ��ahV���#5L�IȔ�G3ǵ`ޔ$|�,�	~.��Y��ƶ��з�!4=_���[���S�<q3i �M���6:�D��5N~ҁ*���@�Ys����*�B펍`$1�k1޺W�A
P���D�j�J=E�a���>���;~��GK��ii��@�M�i����o�ʃ�k��j��";��f���"�26׊�|Lc�y���I��!�7�bw�7�����A�X�fs�9�;־��5k�x~�Ez�^�|�X�<�LIP*A��H��y� �f�B�({��͒K����x�>*ܰ._T�ɢK�
�*v&_�lmX4K�g��6(e&;Etr� 3¿{��̈�ZN@fѓ��� �P��֏0�+E���I�(Lo�$��(��C�M9����#s�48�v 0���57"[7�|.�\x%H�D|���X��a�R�'�H�[��dg����:��!��Ci���fhoo7����.	����8�ҽ�vA@C��Q�4�$���+���'�M�C�WM�d�k��^D-����\'*E�(���ݒ�@�1�G��y�i@�}�:�[�wί�mӣpY�\�ҵ�#ˊr��g����G�x������݋��(��˪+��@A��`���ʚ$���穹dx h����\�g;^�bæ��q$4_�D�ʀ2aib%p�$o�	X��Ȭn�r�Ù�`U &Zpj��wszIr"Z�:&7�2Q6��QK�Ȑ ������qf���@έ��C�̝mFB�6�T�+mm����Ε�GF>. �������N�<z��,����p�gI�$�of�@�C�����>����ޏwv��Abm��JG4�-X?[|��ǘ,s6&+d2��6X�r�i�B�#[z�>�s]�����,�1ؼ���u�	hF��~,����� >'�P����Jh�:=t�Ɉ�$T������a '�-�'�����F"��ȹ0Jْ=�U���"�pԤ��j���.�K�u����D�T�c��I98d]u��N�X'*���"�	e�"�T�'9��v�|H�9�d���y��/�p���}y����"�%�!�m�>@s �Ғ<R�9�m�f���j�Y}��1ֆ&�P������ߒ$��ǚ���sr(�Itp#�2�8	�u�ƨC9��2л��� 37dV��y��P�4n`b�_�̚��]/��"��O�=�64u��]��R%.D�ӟ.@!�����TC�L�6��_""�ڃU���>M��ᰈG��'�}ȉ<��_�ˌ�_#u�p"b�W��q](�oGEnMG�.��,a�I\+�yM[1�E>�R;~�Ͻp�<�#��p�b6���!gL:sA2n\٧u%��L�4JdFB� K�����.3�)G�IW?엙�֠���ߎ�~���#�X��G�I洭�+⌎�%7B��с��1}�+��I��V�`kC�6n��}�6�J���CE@�g�Z�PiJ�����ƃf��?�HM� �U0�|�~N�ЬUd�j��������{��2��8&�X�#�ͽ�r�:H��/JV*ݝ]MX^^��;�j�8֯�Y��c���]����M�H��U"xrF�r���5{��N|���w"csX��X� ���o�f���Hԏ�6��5��8�%�3\1���B+�/�ē���P��N�·?ѣ|/�..~��'2�/~�I<)1�&{�@Bц9H�h3����+ 1�qO��0Z�
t'�q�
�#!�|��1Kҥtw5X�mQ&�c�`r���#��T���$�Uٿ������4�.)�B�ܥ(�d1��&32���k����z�I�rG��=�ʗ�l�:��uԚ� �Q�ăG:Om4�D�XTEԔզ����ö� U9ՌK��L��2�:�|8L&q+�Cڡ�0by��|OL�������lX����ҿC'7�Sb��U0��� �Kr�R1G����j$�Np&����#�xe3s��E�j�[�Jt��H_�"�l�-�Dk:�Ņs�]��\�¶%8�8L|�����0��(�H4���&��X�����;a�31,K<��YK��g2ڳ�x~b�1M�F|E}1ֆ�GX]@�.n��I��T��ĨtR����	��0�:����x<�H$Ir a�9�3c��o���H<|�Ӻ�������?��rd?=��=���n�.���H�ju:Q�R=."hQ�"i}	��ԩht�}��s$�'&G�$ڄ�iM��Y���MqtJF�Q�P�bLLu8�yR�,tZ�r��)�6b�JI�����	0�0F=��AG��RU@��E��h�,l=��ת��j}^|�@{������*j<K�:0G�	09�'�Xn�Is��N�S"��&)&aͥp\*����)�;M�	=q�|C�kJ�&Yb{N�݊�噁�?���=Z�=&G��}�۝+�p�ZV�RprkP!��i�{ɸo�!��0I�]�H�E�J����N�/mGeE��L�u+����|�7���=n�/��@ٸ��hAP1*�i5��ɋ����	�z����n��S"��\�Q0&h���	]I��J� 8Zw�1�nk�|���'��H�>�.�f�$��{'϶��K|#J���~ɥJ�0�����P��V�؆�KOW����aL��My4w�{�����o�Pw�Iu��tY
U���I����<֎i�6L/��'%K�����+F!&\bnٷv�U��F�3��b�N%ڐ#a�x�M9��%Z�`�Q�я`���\I����ə��	ɓ%���6S���eSX��!%T2S���LEZ��T�hI�KҐ0�ldYC��ŜR/�/�Q�ʧ�f�J�|�)�i��8��>�{`��5o���k`�nCgbO@ae�P������R�4��OR�������d{q\��y�T�]�4�p�!9����O	�d(����C
���T�Ս{�q\۸]�(yܪ<�Jrr����%Q�]^�M��ҼK���M�=Z���$���'BK���p��Px�/O��0���?TZ�P�}7XFeǁ4TK
E]:��4��L����H��v�ɒ2���k�A�T� ���#��uIS�U(+��������=s�hi�hP�w,�m�֠
����RP��E�"@I��4�e�%�>�l(+JF��Q������-�y�OPL����d�&v������
��~o�����s�$8��/���|O8BG@��td3{�Ƴ��:��Y���"I���D    IEND�B`� �	      �� ���    0 	        <html>
  <head><title>OSR Test</title></head>
  <style>
  .red_hover:hover {color:red;}
  #li { width: 530px; }
  body {background:rgba(255, 0, 0, 0.5); }
  input {-webkit-appearance: none; }
  #LI11select {width: 75px;}
  </style>
  <script>
  function sendBrowserMessage(paramString) {
    app.sendMessage("osrtest", [paramString]);
  }

  function getElement(id) { return document.getElementById(id); }
  function makeH1Red() { getElement('LI00').style.color='red'; }
  function makeH1Black() { getElement('LI00').style.color='black'; }
  function navigate() { location.href='?k='+getElement('editbox').value; }
  function load() { var select = document.getElementById('LI11select');
    for (var i = 1; i < 21; i++)
      select.options.add(new Option('Option ' + i, i));
  }

  function onEventTest(event) {
    var param = event.type;

    if (event.type == "click")
      param += event.button;

    sendBrowserMessage(param);
  }

  </script>
  <body onfocus='onEventTest(event)' onblur='onEventTest(event)' onload='load();'>
  <h1 id='LI00' onclick="onEventTest(event)">
    OSR Testing h1 - Focus and blur
    <select id='LI11select'>
      <option value='0'>Default</option>
    </select>
    this page and will get this red black
  </h1>
  <ol>
  <li id='LI01'>OnPaint should be called each time a page loads</li>
  <li id='LI02' style='cursor:pointer;'><span>Move mouse
      to require an OnCursorChange call</span></li>
  <li id='LI03' onmousemove="onEventTest(event)"><span>Hover will color this with
      red. Will trigger OnPaint once on enter and once on leave</span></li>
  <li id='LI04'>Right clicking will show contextual menu and will request
      GetScreenPoint</li>
  <li id='LI05'>IsWindowRenderingDisabled should be true</li>
  <li id='LI06'>WasResized should trigger full repaint if size changes.
      </li>
  <li id='LI07'>Invalidate should trigger OnPaint once</li>
  <li id='LI08'>Click and write here with SendKeyEvent to trigger repaints:
      <input id='editbox' type='text' value=''></li>
  <li id='LI09'>Click here with SendMouseClickEvent to navigate:
      <input id='btnnavigate' type='button' onclick='navigate()'
      value='Click here to navigate' id='editbox' /></li>
  <li id='LI10' title='EXPECTED_TOOLTIP'>Mouse over this element will
      trigger show a tooltip</li>
  </ol>
  <br />
  <br />
  <br />
  <br />
  <br />
  <br />
  </body>
</html>
         �� ���    0 	        <html>
<head>
<title>Other Tests</title>
</head>
<body>
<h3>Various other internal and external tests.</h3>
<ul>
<li><a href="http://mudcu.be/labs/JS1k/BreathingGalaxies.html">Accelerated 2D Canvas</a></li>
<li><a href="http://webkit.org/blog-files/3d-transforms/poster-circle.html">Accelerated Layers</a></li>
<li><a href="http://html5advent2011.digitpaint.nl/3/index.html">Cursors</a></li>
<li><a href="http://tests/dialogs">Dialogs</a></li>
<li><a href="http://tests/domaccess">DOM Access</a></li>
<li><a href="http://html5demos.com/drag">Drag & Drop</a></li>
<li><a href="http://www.adobe.com/software/flash/about/">Flash Plugin</a></li>
<li><a href="http://html5demos.com/geo">Geolocation</a></li>
<li><a href="http://www.html5test.com">HTML5 Feature Test</a></li>
<li><a href="http://www.youtube.com/watch?v=siOHh0uzcuY&html5=True">HTML5 Video</a></li>
<li><a href="http://tests/binding">JavaScript Binding</a></li>
<li><a href="http://tests/performance">JavaScript Performance Tests</a></li>
<li><a href="http://tests/window">JavaScript Window Manipulation</a></li>
<li><a href="http://tests/localstorage">Local Storage</a></li>
<li><a href="http://mrdoob.com/lab/javascript/requestanimationframe/">requestAnimationFrame</a></li>
<li><a href="client://tests/handler.html">Scheme Handler</a></li>
<li><a href="http://tests/transparency">Transparency</a></li>
<li><a href="http://webglsamples.googlecode.com/hg/field/field.html">WebGL</a></li>
<li><a href="http://tests/xmlhttprequest">XMLHttpRequest</a></li>
</ul>
</body>
</html>
  �      �� ���    0 	        <!DOCTYPE HTML>
<html>
  <head>
    <title>Performance Tests</title>
    <style>
      body { font-family: Tahoma, Serif; font-size: 9pt; }
    </style>
  </head>
  <body>
    <h1>Performance Tests</h1>
    <input type="button" value="Run Tests" onClick="run();" id="run"/> Filter: <input type="text" size="50" id="filters"/>
    <div><span id="statusBox"></span> <progress id="progressBox" value="0" style="display:none"></progress></div>

    <div style="padding-top:10px; padding-bottom:10px">
    <table id="resultTable" border="1" cellspacing="1" cellpadding="4">
      <thead>
        <tr>
          <td>Name</td>
          <td>Iterations per Run</td>
          <td>Avg (ms)</td>
          <td>Min (ms)</td>
          <td>Max (ms)</td>
          <td>StdDev (ms)</td>
          <td>Runs (ms)</td>
        </tr>
      </thead>
      <!-- result rows here -->
    </table>
    </div>

    <hr width="80%">

    Result 1: <input type="text" size="100" id="result1"/>
    <br/>Result 2: <input type="text" size="100" id="result2"/>
    <br/><input type="button" value="Compare" onClick="compare();" id="compare"/>

    <div style="padding-top:10px; padding-bottom:10px">
    <table id="compareTable" border="1" cellspacing="1" cellpadding="4">
      <thead>
        <tr>
          <td>Name</td>
          <td>Result 1 Avg (ms)</td>
          <td>Result 2 Avg (ms)</td>
          <td>% Diff</td>
        </tr>
      </thead>
      <!-- result rows here -->
    </table>
    </div>

<script type="text/javascript">
function run() {
  var runElement = document.getElementById("run");
  var filtersElement = document.getElementById("filters");
  var compareElement = document.getElementById("compare");
  var result1Element = document.getElementById("result1");
  var result2Element = document.getElementById("result2");

  // Number of runs for each test.
  var testRuns = 10;

  // Delay between test runs.
  var runDelay = 0;

  // Retrieve the list of all tests.
  var allTests = window.GetPerfTests();

  // Populated with the list of tests that will be run.
  var tests = [];
  var currentTest = 0;

  var testList = filtersElement.value.trim();
  if (testList.length > 0) {
    // Include or exclude specific tests.
    var included = [];
    var excluded = [];

    var testNames = testList.split(",");

    // Identify included and excluded tests.
    for (i = 0; i < testNames.length; ++i) {
      var testName = testNames[i].trim();
      if (testName[0] == '-') {
        // Exclude the test.
        excluded.push(testName.substr(1));
      } else {
        // Include the test.
        included.push(testName);
      }
    }

    if (included.length > 0) {
      // Only use the included tests.
      for (i = 0; i < allTests.length; ++i) {
        var test = allTests[i];
        var testName = test[0];
        if (included.indexOf(testName) >= 0)
          tests.push(test);
      }
    } else if (excluded.length > 0) {
      // Use all tests except the excluded tests.
      for (i = 0; i < allTests.length; ++i) {
        var test = allTests[i];
        var testName = test[0];
        if (excluded.indexOf(testName) < 0)
          tests.push(test);
      }
    }
  } else {
    // Run all tests.
    tests = allTests;
  }

  function updateStatusComplete() {
    var statusBox = document.getElementById("statusBox");
    statusBox.innerText = 'All tests completed.';

    runElement.disabled = false;
    filtersElement.disabled = false;
    result1Element.disabled = false;
    result2Element.disabled = false;
    compareElement.disabled = false;
  }

  function updateStatus(test) {
    var statusBox = document.getElementById("statusBox");
    var progressBox = document.getElementById("progressBox");

    if (test.run >= test.totalRuns) {
      statusBox.innerText = test.name + " completed.";
      progressBox.style.display = 'none';
    } else {
      statusBox.innerText = test.name + " (" + test.run + "/" + test.totalRuns + ")";
      progressBox.value = (test.run / test.totalRuns);
      progressBox.style.display = 'inline';
    }
  }

  function appendResult(test) {
    var e = document.getElementById("resultTable");

    // Calculate the average.
    var avg = test.total / test.totalRuns;

    // Calculate the standard deviation.
    var sqsum = 0;
    for (i = 0; i < test.results.length; ++i) {
      var diff = test.results[i] - avg;
      sqsum += diff * diff;
    }
    var stddev = Math.round(Math.sqrt(sqsum / test.totalRuns) * 100.0) / 100.0;

    e.insertAdjacentHTML("beforeEnd", [
        "<tr>",
        "<td>", test.name, "</td>",
        "<td>", test.iterations, "</td>",
        "<td>", avg, "</td>",
        "<td>", test.min, "</td>",
        "<td>", test.max, "</td>",
        "<td>", stddev, "</td>",
        "<td>", test.results.join(", "), "</td>",
        "<tr>"
        ].join(""));

    if (result1Element.value.length > 0)
      result1Element.value += ",";
    result1Element.value += test.name + "=" + avg;
  }

  // Execute the test function.
  function execTestFunc(name) {
    return window.RunPerfTest(name);
  }

  // Schedule the next test.
  function nextTest(test) {
    appendResult(test);
    currentTest++;
    runTest();
  }

  // Schedule the next step for the current test.
  function nextTestStep(test) {
    setTimeout(function () { execTest(test); }, runDelay);
  }

  // Perform the next step for the current test.
  function execTest(test) {
    updateStatus(test);

    if (!test.warmedUp) {
      execTestFunc(test.name);
      test.warmedUp = true;
      return nextTestStep(test);
    }

    if (test.run >= test.totalRuns)
      return nextTest(test);

    var elapsed = execTestFunc(test.name);
    test.results.push(elapsed);

    test.total += elapsed;
    if (!test.min) test.min = elapsed;
    else if (test.min > elapsed) test.min = elapsed;
    if (!test.max) test.max = elapsed;
    else if (test.max < elapsed) test.max = elapsed;

    test.run++;

    return nextTestStep(test);
  }

  function runTest() {
    if (currentTest == tests.length) {
      updateStatusComplete();
      return;
    }

    var test = {
        name: tests[currentTest][0],
        iterations: tests[currentTest][1],
        warmedUp: false,
        total: 0,
        totalRuns: testRuns,
        run: 0,
        results: []
    };
    setTimeout(function () { execTest(test); }, runDelay);
  }

  // Schedule the first test.
  if (tests.length > 0) {
    runElement.disabled = true;
    filtersElement.disabled = true;
    result1Element.value = "";
    result1Element.disabled = true;
    result2Element.disabled = true;
    compareElement.disabled = true;

    runTest();
  }
}

function compare() {
  var result1 = document.getElementById("result1").value.trim();
  var result2 = document.getElementById("result2").value.trim();

  if (result1.length == 0 || result2.length == 0)
    return;

  var r1values = result1.split(",");
  var r2values = result2.split(",");
  for (i = 0; i < r1values.length; ++i) {
    var r1parts = r1values[i].split("=");
    var r1name = r1parts[0].trim();
    var r1val = r1parts[1].trim();

    for (x = 0; x < r2values.length; ++x) {
      var r2parts = r2values[x].split("=");
      var r2name = r2parts[0].trim();
      var r2val = r2parts[1].trim();

      if (r2name == r1name) {
        appendResult(r1name, r1val, r2val);

        // Remove the matching index.
        r2values.splice(x, 1);
        break;
      }
    }
  }
  
  function appendResult(name, r1val, r2val) {
    var e = document.getElementById("compareTable");
 
    // Calculate the percent difference.
    var diff = Math.round(((r2val - r1val) / r1val) * 10000.0) / 100.0;

    e.insertAdjacentHTML("beforeEnd", [
        "<tr>",
        "<td>", name, "</td>",
        "<td>", r1val, "</td>",
        "<td>", r2val, "</td>",
        "<td>", diff, "</td>",
        "<tr>"
        ].join(""));
  }
}
</script>

  </body>
</html>
 �      �� ���    0 	        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Transparency Examples</title>
<style type="text/css">
body {
font-family: Verdana, Arial;
}
img {
opacity:0.4;
}
img:hover {
opacity:1.0;
}
.box_white, .box_black {
font-size: 14px;
font-weight: bold;
text-align: center;
padding: 10px;
display: inline-block;
width: 100px;
}
.box_white {
background-color: white;
border: 2px solid black;
color: black;
}
.box_black {
background-color: black;
border: 2px solid white;
color: white;
}
.box_0 {
opacity: 1.0;
}
.box_25 {
opacity: 0.75;
}
.box_50 {
opacity: 0.5;
}
.box_75 {
opacity: 0.25;
}
.box_100 {
opacity: 0;
}
</style>
</head>
<body>

<h1>Image Transparency</h1>
Hover over an image to make it fully opaque.<br>
<img src="http://www.w3schools.com/css/klematis.jpg" width="150" height="113" alt="klematis" />
<img src="http://www.w3schools.com/css/klematis2.jpg" width="150" height="113" alt="klematis" />

<h1>Block Transparency</h1>
<span class="box_white box_0">White 0%</span> <span class="box_white box_25">White 25%</span> <span class="box_white box_50">White 50%</span> <span class="box_white box_75">White 75%</span> <span class="box_white box_100">White 100%</span>
<br>
<span class="box_black box_0">Black 0%</span> <span class="box_black box_25">Black 25%</span> <span class="box_black box_50">Black 50%</span> <span class="box_black box_75">Black 75%</span> <span class="box_black box_100">Black 100%</span>

</body>
</html>
   �      �� ���    0 	        <html>
<head>
<title>Window Test</title>
<script>
function send_message(test, params) {
  // This will result in a call to OnProcessMessageReceived in window_test.cpp.
  app.sendMessage('WindowTest.' + test, params);
}

function minimize() {
  send_message('Minimize');
}

function maximize() {
  send_message('Maximize');
}

function restore() {
  minimize();
  setTimeout(function() { send_message('Restore'); }, 1000);
}

function position() {
  var x = parseInt(document.getElementById('x').value);
  var y = parseInt(document.getElementById('y').value);
  var width = parseInt(document.getElementById('width').value);
  var height = parseInt(document.getElementById('height').value);
  if (isNaN(x) || isNaN(y) || isNaN(width) || isNaN(height))
    alert('Please specify a valid numeric value.');
  else
    send_message('Position', [x, y, width, height]);
}
</script>
</head>
<body>
<form>
Click a button to perform the associated window action.
<br/><input type="button" onclick="minimize();" value="Minimize">
<br/><input type="button" onclick="maximize();" value="Maximize">
<br/><input type="button" onclick="restore();" value="Restore"> (minimizes and then restores the window as topmost)
<br/><input type="button" onclick="position();" value="Set Position"> X: <input type="text" size="4" id="x" value="200"> Y: <input type="text" size="4" id="y" value="100"> Width: <input type="text" size="4" id="width" value="800"> Height: <input type="text" size="4" id="height" value="600">
</form>
</body>
</html>
H      �� ���    0 	        <html>
<body>
<script language="JavaScript">
function execXMLHttpRequest()
{
  xhr = new XMLHttpRequest();
  xhr.open("GET",document.getElementById("url").value,false);
  xhr.setRequestHeader('My-Custom-Header', 'Some Value');
  xhr.send();
  document.getElementById('ta').value = "Status Code: "+xhr.status+"\n\n"+xhr.responseText;
}
</script>
<form>
URL: <input type="text" id="url" value="http://tests/request">
<br/><input type="button" onclick="execXMLHttpRequest();" value="Execute XMLHttpRequest">
<br/><textarea rows="10" cols="40" id="ta"></textarea>
</form>
</body>
</html>
�      �� ��     	        (       @                                   �  �   �� �   � � ��  ��� ���   �  �   �� �   � � ��  ��� ����������������                wwwwwwwwwwwwwwwpx��������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������pxwwwwwwwwwwwwwxpx��������������pxDDDDDDDDD@    pxDDDDDDDDDH���pxDDDDDDDDDH���pxDDDDDDDDDDDDDDpx��������������pwwwwwwwwwwwwwwwp��������������������������������                                                                                                                                (      �� ��     	        (                                          �  �   �� �   � � ��  ��� ���   �  �   �� �   � � ��  ��� ��������        wwwwwwwpx������px������px������px������px������px������px������pxwwwwwwpxDDD���pxDDDDDDpx������pwwwwwwww��������                                                                �      �� ��     	        (   0   `                             qj� {r�     �R' �Q' �P' �H# �S$ �S% �R& �S& �R& �R' �hC �W! �V" �V" �U# �U$ �iP �Z �Z �Y �X  �X  �^ �] �] �[ �\ �a �` �` �_ �g �g �d �c �l
 �k �i �i �h �i �e) ʌP �b  �b �b �t �s �_ �[ �[ �c �r �v �p �b �q �o �p �x
 �n	 �h	 �w �o �t �y" �}' ߋ, Ȃ3 �E ֎@ ��U ��| ��� �s  �q  �f  �c  �b  �c  �t �u �t �r �j �e �c �c �] �[ �\ �u �v �t �u �t �e �] �\ �v �u �v �u �q �n �o �\ �v �w �u �v �u �w �v �w �w �v �w �x	 �x
 �y �z �z �z �{ �y �w ݂ �t �t �' �( �, �, �- �- �. �0 �0 �0 �0 �1 �3 ی1 �6 �7 �8 �8 �8 �: �= �> �= �@ �? �B �D �E �I �I �I �S �^ ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� Fp� cq� ��� sss ppp iii aaa ``` ___ ]]] [[[ YYY XXX                                                                                                                                                                                                                                                                     ����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������	��������������������������������������������
������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������!�� ��������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������%��������������������������������������������%��$��������������������������������������������$��"��������������������������������������������#��"��������������������������������������������#��"��������������������������������������������#��*��������������������������������������������+��(��������������������������������������������)��'��������������������������������������������'��&��������������������������������������������&��?��������������������������������������������?��<��������������������������������������������=��9��������������������������������������������;��7��������������������������������������������A��63[4]5mm]5\]m]mm5\mm5555555\\\5\\\5m\55\\5ed:���cOXY/P.Z0.0.QR00/ZPP0000000/0PPZR.BI@/DE0, �C��WkV21TSav^8{|}>qooggggggg1`_fhsnHK�{JLp��G���l�����������������������������������������-F�j���Nw~ytMMMMMMUbbrrrrrxxxxxxxxrriUMMMMMMMMMUuzt���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������  ������  ������  ������  �                                                                                                                                                                                                                                                                         �      ������  ������  ������  ������  ������  �      �� ��     	        (       @                             ~r�     YRO �M" �M" �M" �O$ �S( �S) xH0 ~L3 wG0 wH0 rE. xI1 �YB \TP XRO �K �N! �O" �N" �N" �O$ xI0 pE. ZSO �h: �lA �j@ Ȕs Ǘz �B  �@  �i7 ̖q ʕr YSO b_] �F  �E  �C  �D  �E  �C  �n5 �l6 �f> Ηn ɗs [TO �L  �I  �J  �I  �s- �r. �p0 �o1 לj ��_ ՛k Қl Йm ZTO �Y  �K  �I �w* �v, ۞h ٝi �i  �Y  �S  �{$ �z& �x' ߢe ݡf �m  �k  �l  �m  �n  �o  �n  �j  �m  �k  �q �} �$ ކ* ݉* �7 �? ��D Ջ; �@ �Q �b �c �b �b �o  �f  �q �r �x � � � �{ �z �{ �z �u ۀ ܀ ܀ ܁ ܁ ܂ ܃ ܂ ݃ ݄ ܄ ݄ ݅ �! ݆ �$ ݆! �& �' �* �+ �- ۉ* �/ �/ �3 �3 ��5 �6 �5 �7 �7 �8 �; �= �? ԍ7 ��V �p ��� ^][ �Ҫ �Ѵ �϶ �ϴ �ϵ �ͳ �ѷ �ҹ �ӹ �ҹ �Ӹ �ӹ �ս ��� ��� ��� ��� ��� ��� ��� ��� ��� .j� ��� ��� ttt ```                                                                                                                                                                                                                                                                                         ����������������������������������������������������������������������������������������������������������������������������������		
&�������������������������������1������������������������������������������������������������"���������������������������$��.���������������������������#%��-���������������������������0%��:���������������������������?%��9���������������������������>%��8���������������������������=%��7���������������������������;��E���������������������������G@��D���������������������������F@��M���������������������������O@��L���������������������������N2��K���������������������������h2��\���������������������������g2��]���������������������������f2��[�������������������������������I3')+*+)))*))()*+++,6J!54 CBA���jYPQTVTSkllZTTXRTUiHceWda/� i���u����`�������������������_<bm����t^��}zy|yx~���{|yvrrwsqpon������������������������������������������������������������������������������������������������������������������������������������������������   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �  �  ����������������h      �� ��     	        (                                        |WR ��� �Q3 �O1 ��b sP? �Q2 �Q2 �Y9 �^G ײ� �`E ۹� �cE �bE ݹ� ��� �o7 �f@ �gD �eD 㼜 ໝ �Ü 徚 �b �c �c �d �c �d �d �s  �x( ·> �zZ 翘 ��� �g ഄ ��x �$ �& �* �+ �+ �, �- �- �/ �0 �4 �6 ��I ��� ��� ��� ��� �ڽ ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ���  ~~~ }}}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 PPPPPPPPPPPPPPPPPKMNNNNNNNNNNOLO	O&:
OJHHGGGGGGGGHI
OJEEEEEEEEEEFCOJEEEEEEEEEEFCOJEEEEEEEEEEFDOJEFEEEEEEEEEBO%JEEEEEEEEEFFBOJJIIIIJIIIIJJO(@>=77A779?<8;$O' "!)O6530./21+*-,4#4PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP����� ��  ��  ��  ��  ��  ��  �  ��  ��  ��  ��  �� ����������%      �� ��     	        (   0   `                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��H#��P'��Q'��Q'��Q'��Q'�   �   �Q&ݤR&��R&��R'��R&��R&��R&��R&��R&��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��R'�   �   �R&����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������S&�   �   �S%����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������S$�   �   �U$����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������U#�   �   �V"����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������V"�   �   �W!����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������X �   �   �Y����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������X �   �   �Z����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������Z�   �   �\����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������[�   �   �]����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������]�   �   �^����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������_�   �   �`����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������`�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �c����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������c�   �   �d����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������d�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �h����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������i��   �i����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������i��   !�k����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������k��   "�l
����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������l
��   "�n	����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������n	��   "�o����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������p��   "�p����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������q��   !�r����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������w��   �c��_��]��[��\��[��\��\��\��[��[��\��\��\��\��\��[��[��\��\��[��[��[��[��[��[��[��[��[��[��[��[��[��[��[��\��[��[��[��[��[��[��\��]��b��t��   �e��f ��e��c��b��c ��b ��c��b��b ��b��b ��b ��c ��b��b��b��c��c ��c ��b��b��b��b��b��b��b��b��b��c ��c ��c��c ��b ��o�֎@��h	��b��y"��}'��b��e)�qj���iP��t��t��   �j��n��r��s��t��u��t��u��w��u��v��y��{��z��{��z��x
��v��w��w��u��u��u��u��u��u��u��t��t��v��v��v��w��v��E���|�݂��y���U������u�{r��Fp��cq��Ȃ3��w�   �   �o��y��S��^��E��D��I��I��D��:��B��I��6��3��8��?��@��=��>��>��=��=��=��=��=��=��=��8��,��0��0��-��.��(��'��,��0��-��0��1��7��8�ʌP�ߋ,�ی1��q�   J   �w 1�q ��v��z��x	��v��s ��s ��s ��s ��s ��s ��t��t��t��u��u��u��u��u��w��w��w��w��w��w��w��w��u��u��u��t��s ��s ��s ��s ��s ��s ��s ��s ��s ��t��w��x
��v��` W                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ������  ������  ������  �                                                                                                                                                                                                                                                                                                                             ������  ������  ������  ������  ������  �      �� ��     	        (       @                                                                                                                                                                                                                                                                                                                                                                                                     ^   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   i   �G ��>�:��9��8��8��8��9��:��:��:��:��:��:��:��:��:�:�:�:�:�:�:�:�:�:�:�:�:�i2�	�   ,�K��S(��O$��N!��N!��N!��N!��N"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��N"��M"��M"��O$��S)��O"��   1�lA�����������������������������������������������������������������������������������������������������������������ɗs�#�   -�j@�����������������������������������������������������������������������������������������������������������������Ǘz�
�   +�h:�����������������������������������������������������������������������������������������������������������������Ȕs�	 �   +�i7�����������������������������������������������������������������������������������������������������������������ʕr�
 �   +�l6�����������������������������������������������������������������������������������������������������������������̖q�
 �   +�n5�����������������������������������������������������������������������������������������������������������������Ηn�
 �   +�o1�����������������������������������������������������������������������������������������������������������������Йm� �   +�p0�����������������������������������������������������������������������������������������������������������������Қl� �   *�r.�����������������������������������������������������������������������������������������������������������������՛k� �   #�r.�����������������������������������������������������������������������������������������������������������������՛k� �   "�s-�����������������������������������������������������������������������������������������������������������������לj� �   "�v,�����������������������������������������������������������������������������������������������������������������ٝi� �   "�w*�����������������������������������������������������������������������������������������������������������������۞h� �   "�x'�����������������������������������������������������������������������������������������������������������������ݡf� �   "�z&�����������������������������������������������������������������������������������������������������������������ߢe� �   "�{$������������������������������������������������������������������������������������������������������������������b� �   "�$������������������������������������������������������������������������������������������������������������������b� �   "ކ*������������������������������������������������������������������������������������������������������������������c� �   "�}������ս��ҹ��ҹ��ӹ��ӹ��ҹ��ӹ��ӹ��ӹ��ӹ��ҹ��ӹ��ҹ��ҹ��ҹ��ҹ��ҹ��ҹ��Ӹ��϶��ͳ��ѷ��ϵ��ϴ��Ѵ��Ҫ�����ԍ7��   "�Y ��L ��F ��C ��E ��D ��E ��C ��C ��C ��D ��C ��C ��E ��C ��C ��D ��E ��E ��E ��C ��I ��S ��@ ��J ��I ��B ��I��K ��Y ��   $�f ��k ��m ��k ��n ��n ��n ��m ��q��r��r��q��n ��n ��m ��m ��l ��n ��o ��o ��i ��@��b��j ��Q���D��f>�.j��~r���o ��   �n �ۉ*���V��=��;��?��7��3��?��/��/��8��7��5��6��6��-��&��*��'��!��+��3��$��3���5��7���_�Ջ;��x�h   �t b�x�܄�܀��}��{��z��z
��|��z	��y
��|��}��}������~��{��|��z	��y��s ��s ��y��t ��t ��{ ��} ��|��m �                                                                                                                                                                                                                                                                                                                                                                                                       ���������                                                                                                          ������������h      �� ��     	        (                                                                                      
      k   �   �   �   �   �   �   �   �   �   �   �   �   {   O�O&��F#�C!�C!�C!�C!�C!�C!�C!�C!�C!�C!�A �E$�R(�   pְ������������������������������������������������������rE+�   pٵ������������������������������������������������������qD)�   p۵������������������������������������������������������tF'�   p޷������������������������������������������������������wH'�   pḖ�����������������������������������������������������zI&�   p五�����������������������������������������������������}L%�   p滒������������������������������������������������������N%�   p���������������������������������������������������������M!�   pް}��������������������������������������������������ڽ��c>�   p�c��d��c��d��d��d��c��b��c��c��x(��s ��o7�|WR��zW�   R�y��*����������������������$��1�mp   	                                                                                                                                � :�  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ����������v       �� ��k     0	                 �      (   00    �         �       h   00     �%          �        h     �      �� ��	     	        (       @                                   �  �   �� �   � � ��  ��� ���   �  �   �� �   � � ��  ��� ����������������                wwwwwwwwwwwwwwwpx��������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������px�������������pxwwwwwwwwwwwwwxpx��������������pxDDDDDDDDD@    pxDDDDDDDDDH���pxDDDDDDDDDH���pxDDDDDDDDDDDDDDpx��������������pwwwwwwwwwwwwwwwp��������������������������������                                                                                                                                (      �� ��
     	        (                                          �  �   �� �   � � ��  ��� ���   �  �   �� �   � � ��  ��� ��������        wwwwwwwpx������px������px������px������px������px������px������pxwwwwwwpxDDD���pxDDDDDDpx������pwwwwwwww��������                                                                �      �� ��     	        (   0   `                             qj� {r�     �R' �Q' �P' �H# �S$ �S% �R& �S& �R& �R' �hC �W! �V" �V" �U# �U$ �iP �Z �Z �Y �X  �X  �^ �] �] �[ �\ �a �` �` �_ �g �g �d �c �l
 �k �i �i �h �i �e) ʌP �b  �b �b �t �s �_ �[ �[ �c �r �v �p �b �q �o �p �x
 �n	 �h	 �w �o �t �y" �}' ߋ, Ȃ3 �E ֎@ ��U ��| ��� �s  �q  �f  �c  �b  �c  �t �u �t �r �j �e �c �c �] �[ �\ �u �v �t �u �t �e �] �\ �v �u �v �u �q �n �o �\ �v �w �u �v �u �w �v �w �w �v �w �x	 �x
 �y �z �z �z �{ �y �w ݂ �t �t �' �( �, �, �- �- �. �0 �0 �0 �0 �1 �3 ی1 �6 �7 �8 �8 �8 �: �= �> �= �@ �? �B �D �E �I �I �I �S �^ ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� Fp� cq� ��� sss ppp iii aaa ``` ___ ]]] [[[ YYY XXX                                                                                                                                                                                                                                                                     ����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������	��������������������������������������������
������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������!�� ��������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������%��������������������������������������������%��$��������������������������������������������$��"��������������������������������������������#��"��������������������������������������������#��"��������������������������������������������#��*��������������������������������������������+��(��������������������������������������������)��'��������������������������������������������'��&��������������������������������������������&��?��������������������������������������������?��<��������������������������������������������=��9��������������������������������������������;��7��������������������������������������������A��63[4]5mm]5\]m]mm5\mm5555555\\\5\\\5m\55\\5ed:���cOXY/P.Z0.0.QR00/ZPP0000000/0PPZR.BI@/DE0, �C��WkV21TSav^8{|}>qooggggggg1`_fhsnHK�{JLp��G���l�����������������������������������������-F�j���Nw~ytMMMMMMUbbrrrrrxxxxxxxxrriUMMMMMMMMMUuzt���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������  ������  ������  ������  �                                                                                                                                                                                                                                                                         �      ������  ������  ������  ������  ������  �      �� ��     	        (       @                             ~r�     YRO �M" �M" �M" �O$ �S( �S) xH0 ~L3 wG0 wH0 rE. xI1 �YB \TP XRO �K �N! �O" �N" �N" �O$ xI0 pE. ZSO �h: �lA �j@ Ȕs Ǘz �B  �@  �i7 ̖q ʕr YSO b_] �F  �E  �C  �D  �E  �C  �n5 �l6 �f> Ηn ɗs [TO �L  �I  �J  �I  �s- �r. �p0 �o1 לj ��_ ՛k Қl Йm ZTO �Y  �K  �I �w* �v, ۞h ٝi �i  �Y  �S  �{$ �z& �x' ߢe ݡf �m  �k  �l  �m  �n  �o  �n  �j  �m  �k  �q �} �$ ކ* ݉* �7 �? ��D Ջ; �@ �Q �b �c �b �b �o  �f  �q �r �x � � � �{ �z �{ �z �u ۀ ܀ ܀ ܁ ܁ ܂ ܃ ܂ ݃ ݄ ܄ ݄ ݅ �! ݆ �$ ݆! �& �' �* �+ �- ۉ* �/ �/ �3 �3 ��5 �6 �5 �7 �7 �8 �; �= �? ԍ7 ��V �p ��� ^][ �Ҫ �Ѵ �϶ �ϴ �ϵ �ͳ �ѷ �ҹ �ӹ �ҹ �Ӹ �ӹ �ս ��� ��� ��� ��� ��� ��� ��� ��� ��� .j� ��� ��� ttt ```                                                                                                                                                                                                                                                                                         ����������������������������������������������������������������������������������������������������������������������������������		
&�������������������������������1������������������������������������������������������������"���������������������������$��.���������������������������#%��-���������������������������0%��:���������������������������?%��9���������������������������>%��8���������������������������=%��7���������������������������;��E���������������������������G@��D���������������������������F@��M���������������������������O@��L���������������������������N2��K���������������������������h2��\���������������������������g2��]���������������������������f2��[�������������������������������I3')+*+)))*))()*+++,6J!54 CBA���jYPQTVTSkllZTTXRTUiHceWda/� i���u����`�������������������_<bm����t^��}zy|yx~���{|yvrrwsqpon������������������������������������������������������������������������������������������������������������������������������������������������   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �  �  ����������������h      �� ��     	        (                                        |WR ��� �Q3 �O1 ��b sP? �Q2 �Q2 �Y9 �^G ײ� �`E ۹� �cE �bE ݹ� ��� �o7 �f@ �gD �eD 㼜 ໝ �Ü 徚 �b �c �c �d �c �d �d �s  �x( ·> �zZ 翘 ��� �g ഄ ��x �$ �& �* �+ �+ �, �- �- �/ �0 �4 �6 ��I ��� ��� ��� ��� �ڽ ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ��� ���  ~~~ }}}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 PPPPPPPPPPPPPPPPPKMNNNNNNNNNNOLO	O&:
OJHHGGGGGGGGHI
OJEEEEEEEEEEFCOJEEEEEEEEEEFCOJEEEEEEEEEEFDOJEFEEEEEEEEEBO%JEEEEEEEEEFFBOJJIIIIJIIIIJJO(@>=77A779?<8;$O' "!)O6530./21+*-,4#4PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP����� ��  ��  ��  ��  ��  ��  �  ��  ��  ��  ��  �� ����������%      �� ��     	        (   0   `                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��H#��P'��Q'��Q'��Q'��Q'�   �   �Q&ݤR&��R&��R'��R&��R&��R&��R&��R&��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��Q'��R'�   �   �R&����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������S&�   �   �S%����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������S$�   �   �U$����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������U#�   �   �V"����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������V"�   �   �W!����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������X �   �   �Y����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������X �   �   �Z����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������Z�   �   �\����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������[�   �   �]����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������]�   �   �^����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������_�   �   �`����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������`�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �a����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������a�   �   �c����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������c�   �   �d����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������d�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �g����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������g�   �   �h����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������i��   �i����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������i��   !�k����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������k��   "�l
����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������l
��   "�n	����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������n	��   "�o����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������p��   "�p����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������q��   !�r����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������w��   �c��_��]��[��\��[��\��\��\��[��[��\��\��\��\��\��[��[��\��\��[��[��[��[��[��[��[��[��[��[��[��[��[��[��[��\��[��[��[��[��[��[��\��]��b��t��   �e��f ��e��c��b��c ��b ��c��b��b ��b��b ��b ��c ��b��b��b��c��c ��c ��b��b��b��b��b��b��b��b��b��c ��c ��c��c ��b ��o�֎@��h	��b��y"��}'��b��e)�qj���iP��t��t��   �j��n��r��s��t��u��t��u��w��u��v��y��{��z��{��z��x
��v��w��w��u��u��u��u��u��u��u��t��t��v��v��v��w��v��E���|�݂��y���U������u�{r��Fp��cq��Ȃ3��w�   �   �o��y��S��^��E��D��I��I��D��:��B��I��6��3��8��?��@��=��>��>��=��=��=��=��=��=��=��8��,��0��0��-��.��(��'��,��0��-��0��1��7��8�ʌP�ߋ,�ی1��q�   J   �w 1�q ��v��z��x	��v��s ��s ��s ��s ��s ��s ��t��t��t��u��u��u��u��u��w��w��w��w��w��w��w��w��u��u��u��t��s ��s ��s ��s ��s ��s ��s ��s ��s ��t��w��x
��v��` W                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ������  ������  ������  �                                                                                                                                                                                                                                                                                                                             ������  ������  ������  ������  ������  �      �� ��     	        (       @                                                                                                                                                                                                                                                                                                                                                                                                     ^   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   �   i   �G ��>�:��9��8��8��8��9��:��:��:��:��:��:��:��:��:�:�:�:�:�:�:�:�:�:�:�:�:�i2�	�   ,�K��S(��O$��N!��N!��N!��N!��N"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��M"��N"��M"��M"��O$��S)��O"��   1�lA�����������������������������������������������������������������������������������������������������������������ɗs�#�   -�j@�����������������������������������������������������������������������������������������������������������������Ǘz�
�   +�h:�����������������������������������������������������������������������������������������������������������������Ȕs�	 �   +�i7�����������������������������������������������������������������������������������������������������������������ʕr�
 �   +�l6�����������������������������������������������������������������������������������������������������������������̖q�
 �   +�n5�����������������������������������������������������������������������������������������������������������������Ηn�
 �   +�o1�����������������������������������������������������������������������������������������������������������������Йm� �   +�p0�����������������������������������������������������������������������������������������������������������������Қl� �   *�r.�����������������������������������������������������������������������������������������������������������������՛k� �   #�r.�����������������������������������������������������������������������������������������������������������������՛k� �   "�s-�����������������������������������������������������������������������������������������������������������������לj� �   "�v,�����������������������������������������������������������������������������������������������������������������ٝi� �   "�w*�����������������������������������������������������������������������������������������������������������������۞h� �   "�x'�����������������������������������������������������������������������������������������������������������������ݡf� �   "�z&�����������������������������������������������������������������������������������������������������������������ߢe� �   "�{$������������������������������������������������������������������������������������������������������������������b� �   "�$������������������������������������������������������������������������������������������������������������������b� �   "ކ*������������������������������������������������������������������������������������������������������������������c� �   "�}������ս��ҹ��ҹ��ӹ��ӹ��ҹ��ӹ��ӹ��ӹ��ӹ��ҹ��ӹ��ҹ��ҹ��ҹ��ҹ��ҹ��ҹ��Ӹ��϶��ͳ��ѷ��ϵ��ϴ��Ѵ��Ҫ�����ԍ7��   "�Y ��L ��F ��C ��E ��D ��E ��C ��C ��C ��D ��C ��C ��E ��C ��C ��D ��E ��E ��E ��C ��I ��S ��@ ��J ��I ��B ��I��K ��Y ��   $�f ��k ��m ��k ��n ��n ��n ��m ��q��r��r��q��n ��n ��m ��m ��l ��n ��o ��o ��i ��@��b��j ��Q���D��f>�.j��~r���o ��   �n �ۉ*���V��=��;��?��7��3��?��/��/��8��7��5��6��6��-��&��*��'��!��+��3��$��3���5��7���_�Ջ;��x�h   �t b�x�܄�܀��}��{��z��z
��|��z	��y
��|��}��}������~��{��|��z	��y��s ��s ��y��t ��t ��{ ��} ��|��m �                                                                                                                                                                                                                                                                                                                                                                                                       ���������                                                                                                          ������������h      �� ��     	        (                                                                                      
      k   �   �   �   �   �   �   �   �   �   �   �   �   {   O�O&��F#�C!�C!�C!�C!�C!�C!�C!�C!�C!�C!�A �E$�R(�   pְ������������������������������������������������������rE+�   pٵ������������������������������������������������������qD)�   p۵������������������������������������������������������tF'�   p޷������������������������������������������������������wH'�   pḖ�����������������������������������������������������zI&�   p五�����������������������������������������������������}L%�   p滒������������������������������������������������������N%�   p���������������������������������������������������������M!�   pް}��������������������������������������������������ڽ��c>�   p�c��d��c��d��d��d��c��b��c��c��x(��s ��o7�|WR��zW�   R�y��*����������������������$��1�mp   	                                                                                                                                � :�  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ����������v       �� ��l     0	                 �  	    (  
 00    �         �       h   00     �%          �        h            ��	 ��m     0 	         ? h   � / h   �       �� ��g     0	        � �        � K     A b o u t    S y s t e m       P     	    ��� ��k   � P    1 
 w  ����� c e f c l i e n t   V e r s i o n   1 . 0       P    1  w  ����� C o p y r i g h t   ( C )   2 0 0 8        P    �     ��� O K       l       �� ��     0	                      	 c e f c l i e n t           	 C E F C L I E N T  C E F C L I E N T - O S R - W I D G E T   