#include <stdio.h>

int main()
{
system ("chmod 777 wmctrl");
system("sh createoriginal.sh");
system("gcc -g -o xmodmapobj  ModifyXmodmap.c");
system ("chmod 777 xmodmapobj");
system("./xmodmapobj");
system("sh loadmodified.sh");
system("xmodmap -e \"pointer = 1 9 8 7 6 5 4 3 2\"");
system("xmodmap -e \"keycode 0x09 = A\"");
system("sh print_screen_disable.sh");
system("sh processcheck.sh");
//system("find /usr/bin -type f -name \"gnome-panel-screenshot\" -exec chmod 600 {} \\;");
return 0;
}

