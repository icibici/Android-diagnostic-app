----------------------------------------------------------------------------------------------------

**WARNING! DO NOT CONNECT THE SMARTPHONEBCI IF YOUR PHONE IS CHARGING! THIS COULD KILL YOU IF THERE'S A MALFUNCTION**
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------

# ICIBICI

[Icibici](https://icibici.github.io/smartphone-bci-hardware) is a hardware / software platform born with the intention of Developing an EEG under £20.

This is the **Software Repository** for the smartphone BCI Project Android App

This repository is part of a Hardware - Software system:

* [icibici](https://github.com/icibici/smartphone-bci)
* [icibici-hardware](https://github.com/icibici/smartphone-bci-hardware)


## The Android app

This is the Android app for the [icibici project] (https://icibici.github.io/site/).
It allows to visualise the data read from the [Icibici hardware ] (https://github.com/icibici/smartphone-bci-hardware/) and it is under heavy development.

There are three tabs (change from one to another by swiping) 

EEG frequency analysis from the bipolar electrode:
![Frequency analysis](https://cloud.githubusercontent.com/assets/1718009/17438363/61108920-5b1a-11e6-872d-3c7378568f4d.png)

Power of the alpha band over time
![Power of the alpha band ](https://cloud.githubusercontent.com/assets/1718009/17438367/635871ca-5b1a-11e6-821e-49dd994b4856.png)

There should be two big peaks in the sanity check (electrodes not yet attached to head), one around 0 and one around 50Hz (or 60Hz depending on your electrical mains)
![Sanity check](https://cloud.githubusercontent.com/assets/1718009/17438370/653b9c4c-5b1a-11e6-8630-899ffeed751d.png)



TODO:

 - Use a C/C++ fft implementation.
 - Move the driver functionality to a separate project so it can be easily used in other apps.
 - The code is a bad state due to different rushes and time constrains, it needs a clean up. 
 - The alpha viewer should baseline to the self band not to the relative values 
