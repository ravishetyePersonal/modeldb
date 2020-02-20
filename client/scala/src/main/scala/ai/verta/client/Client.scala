package ai.verta.client

import sys.env

case class ClientConnection(host: String, ignoreConnErr: Boolean = false, maxRetries: Int = 5, auth: ClientAuth = null)

object ClientConnection {
    def fromEnvironment(): ClientConnection = {
        val auth = ClientAuth.fromEnvironment()
        val host = sys.env("VERTA_HOST")
        return new ClientConnection(host, auth=auth)
    }
}

case class ClientAuth(email: String, devKey: String)

object ClientAuth {
    def fromEnvironment(): ClientAuth = {
        val email = sys.env("VERTA_EMAIL")
        val devKey = sys.env("VERTA_DEV_KEY")
        return new ClientAuth(email, devKey)
    }
}

class Client(conn: ClientConnection) {

}
