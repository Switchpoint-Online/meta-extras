#include <stdio.h>
#include <stdlib.h>
#include <libusb-1.0/libusb.h>

static int hotplug_callback(libusb_context *ctx, libusb_device *dev, libusb_hotplug_event event, void *user_data);

int main(void)
{
    libusb_context *ctx = NULL;
    int rc = libusb_init(&ctx);

    if (rc < 0) {
        fprintf(stderr, "Error initializing libusb: %s\n", libusb_error_name(rc));
        return EXIT_FAILURE;
    }

    libusb_hotplug_callback_handle hp[2];
    libusb_hotplug_register_callback(ctx,
                                      LIBUSB_HOTPLUG_EVENT_DEVICE_ARRIVED,
                                      LIBUSB_HOTPLUG_ENUMERATE,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      hotplug_callback,
                                      NULL,
                                      &hp[0]);

    libusb_hotplug_register_callback(ctx,
                                      LIBUSB_HOTPLUG_EVENT_DEVICE_LEFT,
                                      LIBUSB_HOTPLUG_ENUMERATE,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      LIBUSB_HOTPLUG_MATCH_ANY,
                                      hotplug_callback,
                                      NULL,
                                      &hp[1]);

    while (1) {
        rc = libusb_handle_events(ctx);
        if (rc < 0) {
            fprintf(stderr, "libusb_handle_events() failed: %s\n", libusb_error_name(rc));
            break;
        }
    }

    libusb_exit(ctx);
    return EXIT_SUCCESS;
}

static int hotplug_callback(libusb_context *ctx, libusb_device *dev, libusb_hotplug_event event, void *user_data)
{
    switch (event) {
        case LIBUSB_HOTPLUG_EVENT_DEVICE_ARRIVED:
            printf("USB device plugged in.\n");
            break;
        case LIBUSB_HOTPLUG_EVENT_DEVICE_LEFT:
            printf("USB device unplugged.\n");
            break;
    }
    return 0;
}
