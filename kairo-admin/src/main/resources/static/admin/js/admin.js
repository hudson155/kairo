import { Application } from "../vendor/stimulus.js"

import RequestController from "./controllers/request_controller.js"
import SqlController from "./controllers/sql_controller.js"
import CopyController from "./controllers/copy_controller.js"
import RefreshController from "./controllers/refresh_controller.js"
import HeadersController from "./controllers/headers_controller.js"
import SidebarController from "./controllers/sidebar_controller.js"

const application = Application.start()
application.register("request", RequestController)
application.register("sql", SqlController)
application.register("copy", CopyController)
application.register("refresh", RefreshController)
application.register("headers", HeadersController)
application.register("sidebar", SidebarController)
