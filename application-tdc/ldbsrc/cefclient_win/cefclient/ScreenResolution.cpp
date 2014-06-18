#include "stdafx.h"
#include < windows.h >
#include "conio.h"
 
void ListDisplayDevices()
{
    int index=0;
    DISPLAY_DEVICE dd;
    dd.cb = sizeof(DISPLAY_DEVICE);
 
    while (EnumDisplayDevices(NULL, index++, &dd, 0))
    {
        if (dd.StateFlags & DISPLAY_DEVICE_PRIMARY_DEVICE) printf("* ");
        printf("%s, %s\n", dd.DeviceName, dd.DeviceString);
    }
}
 
DISPLAY_DEVICE GetPrimaryDevice()
{
    int index=0;
    DISPLAY_DEVICE dd;
    dd.cb = sizeof(DISPLAY_DEVICE);
 
    while (EnumDisplayDevices(NULL, index++, &dd, 0))
    {
        if (dd.StateFlags & DISPLAY_DEVICE_PRIMARY_DEVICE) return dd;
    }
    return dd;
}
 
void ListDisplaySettings(int index)
{
    DISPLAY_DEVICE dd;
    dd.cb = sizeof(DISPLAY_DEVICE);
    if (!EnumDisplayDevices(NULL, index, &dd, 0))
    {
        printf("EnumDisplayDevices failed:%d\n", GetLastError());
        return;
    }
 
    DISPLAY_DEVICE monitor;
    monitor.cb = sizeof(DISPLAY_DEVICE);
    if (!EnumDisplayDevices(dd.DeviceName, index, &monitor, 0))
    {
        printf("EnumDisplayDevices failed:%d\n", GetLastError());
        return;
    }
 
    DEVMODE dm;
    dm.dmSize = sizeof(DEVMODE);
 
    if (!EnumDisplaySettings(dd.DeviceName, ENUM_CURRENT_SETTINGS, &dm))
    {
        printf("EnumDisplaySettings failed:%d\n", GetLastError());
        return;
    }
 
    printf("Device name: %s\n", dd.DeviceName);
    printf("Monitor name: %s\n", monitor.DeviceID);
    printf("Refresh rate, in hertz: %d\n", dm.dmDisplayFrequency);
    printf("Color depth: %d\n", dm.dmBitsPerPel);
    printf("Screen resolution, in pixels: %d x %d\n", 
        dm.dmPelsWidth, dm.dmPelsHeight);
}
 
void SetDisplayDefaults()
{
    //changes the settings of the default display device
    //to the default mode
    ChangeDisplaySettings(NULL, 0);
}
 
BOOL SetDisplayResolution(long PelsWidth, long PelsHeight)
{
    DISPLAY_DEVICE dd = GetPrimaryDevice();
    DEVMODE dm;
    dm.dmSize = sizeof(DEVMODE);
    if (!EnumDisplaySettings(dd.DeviceName, ENUM_CURRENT_SETTINGS, &dm))
    {
        printf("EnumDisplaySettings failed:%d\n", GetLastError());
        return FALSE;
    }
 
    dm.dmPelsWidth = PelsWidth;
    dm.dmPelsHeight = PelsHeight;
    dm.dmFields = (DM_PELSWIDTH | DM_PELSHEIGHT);
    if (ChangeDisplaySettings(&dm, CDS_TEST) !=DISP_CHANGE_SUCCESSFUL)
    {
        printf("\nIllegal graphics mode: %d\n", GetLastError());
        return FALSE;
    }
 
    return (ChangeDisplaySettings(&dm, 0)==DISP_CHANGE_SUCCESSFUL);
}
 
void _tmain()
{
    ListDisplayDevices();
    ListDisplaySettings(0);
 
    if (SetDisplayResolution(800, 600))
    {
        printf("\nPress Enter to return to default mode.\n");
        getch();
        SetDisplayDefaults();
    }
    else
    {
        printf("\nPress Enter to close this window.\n");
        getch();
    }
    return;
}