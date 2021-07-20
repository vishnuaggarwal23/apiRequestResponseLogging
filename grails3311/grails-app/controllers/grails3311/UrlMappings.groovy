package grails3311

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/respondSave/${id}"(controller: 'api', action: 'respondSave', method: 'POST')
        "/renderSave/${id}"(controller: 'api', action: 'renderSave', method: 'POST')
    }
}
