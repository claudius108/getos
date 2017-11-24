xquery version "3.1";

declare namespace map = "http://www.w3.org/2005/xpath-functions/map";

let $document := /
    
let $metadata := $document//meta[@name]

return map:merge(
	for $meta-element in $metadata
	
	return map:entry($meta-element/@name, $meta-element/@content/data(.))
)