#include <stdio.h>
#include <libusb-1.0/libusb.h>

#define VENDOR_ID 0x1234
#define PRODUCT_ID 0x5678

static int print_device(libusb_device *dev);

static int print_device(libusb_device *dev) {
    struct libusb_device_descriptor desc;
    int r = libusb_get_device_descriptor(dev, &desc);
    if (r < 0) {
        printf("Failed to get device descriptor\n");
        return -1;
    }
    printf("VID: %04x, PID: %04x\n", desc.idVendor, desc.idProduct);
    return 0;
}

static int hotplug_callback(libusb_context *ctx, libusb_device *dev, libusb_hotplug_event event, void *user_data) {
    printf("Device arrived/removed\n");
    print_device(dev);
    return 0;
}

int main(int argc, char *argv[]) {
    libusb_device **devs;
    libusb_context *ctx = NULL;
    int r;
    ssize_t cnt;
    r = libusb_init(&ctx);
    if (r < 0)
        return r;
    libusb_set_debug(ctx, 3);
    cnt = libusb_get_device_list(ctx, &devs);
    if (cnt < 0)
        return (int) cnt;
    printf("Listening for devices...\n");
    libusb_device_handle *handle = NULL;
    r = libusb_hotplug_register_callback(NULL, LIBUSB_HOTPLUG_EVENT_DEVICE_ARRIVED, LIBUSB_HOTPLUG_NO_FLAGS, VENDOR_ID, PRODUCT_ID, LIBUSB_HOTPLUG_MATCH_ANY, hotplug_callback, NULL, &handle);
    if (r != LIBUSB_SUCCESS) {
        fprintf(stderr, "Error registering callback\n");
        return r;
    }
    while (1) {
        int i;
        for (i = 0; i < cnt; i++) {
            r = print_device(devs[i]);
            if (r < 0)
                break;
        }
        if (r < 0) {
            printf("Failed to print device\n");
            break;
        }
        libusb_handle_events(ctx);
    }
    libusb_free_device_list(devs, 1);
    libusb_exit(ctx);
    return 0;
}

