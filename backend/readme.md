Keystore generation for SSL:

`keytool -genkeypair -alias bufunfa -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore bufunfa.p12 -validity 3650`

Then `application.properties`:
```properties
server.ssl.key-store-type=PKCS12
server.ssl.key-store=bufunfa.p12
server.ssl.key-store-password=password
server.ssl.key-alias=bufunfa
security.require-ssl=true
```
