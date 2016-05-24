{
	"profileKey" : "vyo_robot",
	"profileName" : "Vyo Robot",
	"channelName" : "vyo_robot",
	"image" : "imgs/vyo.jpg",
	"purpose" : "A social robot designed to manage your smart home.",
	"platform" : "Raspberry Pi",
	"events": {
		"headnod" : {	
			"name" : "headnod",
			"className": "vyo.events.HeadNodEvent",
			"methodName": "nodHead"
		},
		"glowled" : {
			"name" : "glowled",
			"className" : "vyo.events.BlinkLedEvent",
			"methodName" : "blinkLed"
		}
	},
	"sequences" : {}
}