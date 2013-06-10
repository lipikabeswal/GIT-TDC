cmd_out/Release/locales := ln -f "Resources/locales" "out/Release/locales" 2>/dev/null || (rm -rf "out/Release/locales" && cp -af "Resources/locales" "out/Release/locales")
