#include <stdio.h>

int main()
{
system("xmodmap -e \"pointer = 1 2 3 4 5 6 7 8 9\"");
system("sh loadoriginal.sh");
//system("sh print_screen_enable.sh");
//system("find /usr/bin -type f -name \"gnome-panel-screenshot\" -exec chmod 755 {} \\;");
return 0;
}
