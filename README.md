# android-docker-search
:whale: Docker Search on Android

# Docker

Uses Registry Hub REST API v1.  
In particular :
- Search : [GET /v1/search](https://docs.docker.com/v1.6/reference/api/registry_api/#search)
- List repository tags : [GET /v1/repositories/(namespace)/(repository)/tags](https://docs.docker.com/v1.6/reference/api/registry_api/#list-repository-tags)

See [reference documentation](https://docs.docker.com/v1.6/reference/api/registry_api/).

# REST Client

Uses [Retrofit 2](https://square.github.io/retrofit/) and [Jackson 2](https://github.com/FasterXML/jackson)

