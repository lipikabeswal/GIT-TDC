#include <stdio.h>

int main()
{
system("sh createoriginal.sh");
system("gcc -g -o xmodmapobj  ModifyXmodmap.c");
system ("chmod 777 xmodmapobj");
system("./xmodmapobj");
system("sh loadmodified.sh");
system("sh print_screen_disable.sh");
system("sh processcheck.sh");
//system("find /usr/bin -type f -name \"gnome-panel-screenshot\" -exec chmod 600 {} \\;");
return 0;
}

