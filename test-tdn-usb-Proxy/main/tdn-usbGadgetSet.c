#include <stdio.h>
#include <libusb-1.0/libusb.h>


int main(int argc, char *argv[])
{
    libusb_device **devs;
    libusb_context *ctx = NULL;
    int r;
    ssize_t cnt;

    r = libusb_init(&ctx);
    if (r < 0)
    {
        fprintf(stderr, "Failed to initialize libusb: %s\n", libusb_error_name(r));
        return 1;
    }

    cnt = libusb_get_device_list(ctx, &devs);
    if (cnt < 0)
    {
        fprintf(stderr, "Failed to get device list: %s\n", libusb_error_name(cnt));
        libusb_exit(ctx);
        return 1;
    }

    printf("Number of devices: %d\n", (int)cnt);

    for (int i = 0; i < cnt; i++)
    {
        libusb_device *dev = devs[i];
        struct libusb_device_descriptor desc;

        r = libusb_get_device_descriptor(dev, &desc);
        if (r < 0)
        {
            fprintf(stderr, "Failed to get device descriptor: %s\n", libusb_error_name(r));
            continue;
        }

        printf("Device %d - VID/PID: %04x:%04x\n", i, desc.idVendor, desc.idProduct);
    }

    libusb_free_device_list(devs, 1);
    libusb_exit(ctx);
    return 0;
}
