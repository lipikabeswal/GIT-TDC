cmd_out/Release/cef.pak := ln -f "Resources/cef.pak" "out/Release/cef.pak" 2>/dev/null || (rm -rf "out/Release/cef.pak" && cp -af "Resources/cef.pak" "out/Release/cef.pak")
