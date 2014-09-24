#!/bin/bash
xmodmap -e 'keycode 135 = NoSymbol' &
./wmctrl -r "Presentation Canvas" -b add,above &
xmodmap -e "keycode 107 = Pause" &
xmodmap -e "keycode 133 =" &

