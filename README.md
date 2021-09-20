# Building Docker Images:

## Listener
`docker build --target listener -t vino-listener:1.0.0 .`

## API
`docker build --target rest-api -t vino-api:1.0.0 .`

### Convenience Script
I included a simple script to build both images at once and tag them properly for docker-compose:

`$ ./build-all.sh`