this.payloadParser = function (parser) {
    parser.delimited("\r\n")
        .handler(function(buffer){
            parser.result(buffer.toString("UTF-8")).complete();
        });
}