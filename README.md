# Load Balancer

Just an assignment got from a job hiring process.

See `requirements.pdf` file for the instructions.

## How to run

Change your current working directory to the project root folder and simply run:

    $ ./gradlew run

# Requirements

## Overview

* A load balancer distributes incoming requests to a list of providers
* Return the value from the provider to the original caller

## Steps

1. Implement a `Provider.get(): String` returning a unique ID of a provider
2. Register a list of (maximum 10) providers to the load balancer
3. Random Algorithm: The `get()` method should randomly select any provider
4. Round Robin Algorithm: Sequentially return one of the providers
5. Node management: Specific providers can be in/excluded
6. Heart beat 1: On a given interval, all providers should be `check()`ed if still alive, if not they should be excluded
7. Heart beat 2: A provider can be re-included, once it is healthy again for 2 consecutive times
8. Cluster: `y` = number of parallel requests per provider, `n` = alive providers, block incoming requests when current processed requests == `y*n`

## Non functional requirements

Pay attention to the following:

* Code structure (principles, separation of concerns, low-coupling/high-cohesion, design patterns etc.)
* Evidences that the code does what it's supposed to do
* Readability of the code
* Flexibility of the code towards potential evolution
* YAGNI/KISS/DRY
