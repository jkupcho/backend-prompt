## Building Docker Images:

### Listener
`docker build --target listener -t vino-listener:1.0.0 .`

### API
`docker build --target rest-api -t vino-api:1.0.0 .`

### Convenience Script
I included a simple script to build both images at once and tag them properly for docker-compose:

`$ ./build-all.sh`

## Other stuff

### Queueing
I chose RabbitMQ for the messaging broker just for its simplicity. The apps use a point-to-point queue, ideally with a more fleshed
out application, there would probably be some sort of fan-out implementation in the case of multiple consumers to the messages. In the case
of RabbitMQ an exchange with multiple queues to allow for multiple independent consumers.

### Event Persistence
I just used a simple JPA to PostgreSQL persistence layer. For the event payload itself, I used a `jsonb` data type in the database.
This event storage might actually lend itself more to an object store due to the payload being arbitrary, but at least with 
`jsonb`, you can index fields and query.

### Environment Variable Filtering
The `FILTER_TYPE` environment variable allows for filtering on the message event type. This is set via `filter.type` property, and
defaults to an empty string if not provided.

### Rate Limit Implementation
I've implemented a super simple API Gateway, it uses Redis for tracking the number of requests on a fake API Key. It should rate limit
based off the configured properties `rate.limit` defaulted to `5`, and `rate.seconds` defaulted to `60`. So, the application should only
allow for 5 requests every minute.

In a production environment, I'd actually move this rate limiting upstream of the application into its own process / application. This way, a much more thin API Gateway 
could easily be scaled up and down to deal with increased load, and leave the more resource intensive application to not get 
bogged down using up resources on a spike of incoming traffic just for checking rate limits.

### Structured Logging
The `common` project puts a `logback.xml` configuration for JSON based log messages. Since each project compiles `common` on
to its own classpath, this configuration comes in by default.

### Request Validation
There's some simple validation to make sure both the `type` and `payload` are provided to the `/event` endpoint, and returns
a `400 Bad Request` if either are not.

### Unit Tests
I tested mostly the API layer for some regression as well as negative testing. I ended up mocking the `GatewayService` for the 
web layer just for ease. I also added a few `RedisGatewayService` tests, just to verify the logic a bit.

### Docker Images
Once images are built, everything should be working with a `docker-compose up`. The schema for the database via dropping a sql
file to /docker-entrypoint-initdb.d within the image.