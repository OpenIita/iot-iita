parser.delimited("\r\n")
    .handler(function(buffer){
        parser.result(parser.newBuffer().toString("UTF-8")).complete();
    });