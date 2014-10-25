# Tablet App

The tablet app consists of a service and a launcher.

## Wi-Fi Service

The Wi-Fi service sits on the tablet and waits for the the access point of the competitors robot to appear and adds a network configuration for it. The service reads this information from a file in `/sdcard/wifi` and is generated during the factory reset process which can be found in the `tablet/tools` repository.

## 'Student Robotics' launcher

The launcher provides a "Student Robotics" icon in the app drawer and when opened it will check the tablet is connected to a Wi-Fi access point and then attempt to launch the browser to either `http://robot.sr/` and `http://robot/` depending on which one works.

