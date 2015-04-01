# minecraft-atopia

[![Join the chat at https://gitter.im/UtopiaDevelopersPartnership/minecraft-atopia](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/UtopiaDevelopersPartnership/minecraft-atopia?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Proof-of-concept system for blockchain-controlled minecraft servers.

##Installation and Usage

Not ment to be used yet, but it will remain stable and can be downloaded.

###Pre-built

I will add dist/ folder with a pre-built jar, dependencies, and instructions on how to set up a server + the node.js server that does the blockchain stuff.


## Dev

###Build from sources

Use `forge-1.8-11.14.1.1334` sources. Needs [jsonrpc2-base](http://software.dzhuvinov.com/json-rpc-2.0-base.html). Instructions can be found on the site. Using `jsonrpc2-base-1.36.jar` and dependency `json-smart-1.3.1.jar`. Those will be put in dist/ when I add that. Don't forget to modify the build script to include them, and set the version etc.

###Functionality

The `BlockchainManager` class is the entry point into the system. It's set up in the main mod class along with the FML event listeners and such (mod class is very light). BlockchainManager communicates with the blockchain indirectly through the node.js server, which has all the blockchain specific code in it. This means no transactions or ecc stuff or any of that in the mod code.

#### calls

The triggers for calling on the node.js server is minecraft events, and could be anything. The first functionality I added was listening to player login and logout events, and then writing those events into a smart contract. The pipeline for a login event is basically this:

1. Player "Steve" logs in. This triggers the event listener for `PlayerLoggedInEvent`.
2. The event listener takes the player name, and event type (which is a string, and in this case "login"), and passes that on to the blockchain manager's `registerPlayerEvent` function. This function packs the data up into a json-rpc request object and sends it to the node.js server through `RpcClient`.
3. The node server receives the call and processes it. 
4. The results of the node.js servers work is packaged into a response object and is sent back.
5. The `RpcClient` reads the message and passes it on to the blockchain manager that has functions for handling it. In the case of player login, it will receive a message that tells whether or not the data was successfully written into the blockchain database.
6. If the request was succesful, a chat message will go out to all players telling them that 'Steve' just logged in.

#### Blockchain data

Since this is a real time application, it is not possible to send a request to the node.js server every time data has to be read from the blockchain. Instead, every time the data in the blockchain is updated, the data is sent to the minecraft server so that it can keep a local copy of the state. This means that basically all calls that goes out to the node.js server is whenever the blockchain needs to be updated - never to read. 

#### Concurrency

Integration between the websocket stuff, blockchain manager and minecraft is fairly simple. Every time an event comes in from the node.js server it is wrapped by an `Action` and added to an action queue (`ConcurrentLinkedQueue`). The queue is polled until empty every Nth server tick (N is 10 atm. but that's just a starting point), and thus it is synced with the minecraft update loop.

Right now, the DataCache access is controlled separately, but i might wrap write operations inside actions as well. Need to look that over before deciding how it will be used. It will also be backed by a more efficient system at some point, such as a MapDB in-memory database.

## TODOs (not necessarily in this order)

- Build/run scripts to start blockchain client + node.js server + minecraft server with one command.
- Add the config file.
- Finish the basic jUnit test suite and put in src/tests.
- Build automation (dist jars).
- Tags.
- Documentation.
- Finalize the blockchain stuff when the clients are stable again.
- Clean up.
- Optimize.
