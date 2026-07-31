workflows:
  android-build:
    name: Compilar APK Medidores
    max_build_duration: 30
    instance_type: mac_mini_m1
    scripts:
      - name: Compilar instalador APK
        script: |
          gradle assembleDebug
    artifacts:
      - app/build/outputs/apk/**/*.apk
