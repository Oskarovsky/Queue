package com.oskarro.queue.model

enum class Stage(name: String) {
    NEW("NEW"),
    BACKLOG("BACKLOG"),
    IN_PROGRESS("IN-PROGRESS"),
    WAITING("WAITING"),
    DONE("DONE");
}
