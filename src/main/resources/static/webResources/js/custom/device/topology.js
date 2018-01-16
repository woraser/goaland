/**
 * 
 */
var game = new Phaser.Game(1150, 660, Phaser.AUTO, 'topology', {
	preload : preload,
	create : create,
	update : update,
});

function preload() {
	game.load.image('sky', '/webResources/img/device/device-topo-small.png');
	game.load.image('ground', '/webResources/img/device/box.png');
	game.load.image('button', '/webResources/img/device/topo-button.png');
	game.stage.backgroundColor = '#4D4948';
}
var platforms;
var button;
var scoreText11;
var scoreText22;
var scoreText33;
var scoreText44;
var scoreText55;
var scoreText66;

function create() {

	// A simple background for our game
	game.add.sprite(80, 160, 'sky');

	//button = game.add.button(730, 600, 'button', actionOnClick, this, 2, 1, 0);

	// The platforms group contains the ground and the 2 ledges we can jump on
	platforms = game.add.group();

	var scoreText = game.add.text(160, 115, '蓄冷槽上端入口温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText11 = game.add.text(180, 135, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(50, 235, '蓄冷槽下端入口温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText22 = game.add.text(70, 255, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(360, 65, '新增空调主机出口温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText33 = game.add.text(380, 85, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(510, 135, '放冷板换入口温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText44 = game.add.text(530, 155, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(560, 235, '放冷板换出口温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText55 = game.add.text(580, 255, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(690, 335, '放冷端回水温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	scoreText66 = game.add.text(710, 355, '642.4', {
		font: '14px Arial',
		fill: '#66A9D2'
	});

	var scoreText = game.add.text(770, 25, '温度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	var scoreText = game.add.text(830, 22, '60', {
		font: '18px Arial',
		fill: '#FFF'
	});
	var scoreText = game.add.text(880, 25, '℃', {
		font: '14px Arial',
		fill: '#FFF'
	});

	var scoreText = game.add.text(770, 65, '湿度', {
		font: '14px Arial',
		fill: '#FFF'
	});
	var scoreText = game.add.text(830, 62, '50', {
		font: '18px Arial',
		fill: '#FFF'
	});
	var scoreText = game.add.text(880, 65, '℃', {
		font: '14px Arial',
		fill: '#FFF'
	});

	// Here we create the ground.
	var ground = platforms.create(40, 220, 'ground');
	// Scale it to fit the width of the game (the original sprite is 400x32 in
	// size)控制长度
	ground.scale.setTo(1, 1);

	// Now let's create two ledges控制位置
	var bb = platforms.create(150, 100, 'ground');
	var aa = platforms.create(350, 50, 'ground');
	var cc = platforms.create(500, 120, 'ground');
	var cc = platforms.create(550, 220, 'ground');
	var dd = platforms.create(680, 320, 'ground');
	
	game.time.desiredFps = 1;
	
	setInterval(function() {
		updateText()
	}, 1000);
	
}

function update() {
	
}

function updateText() {
	scoreText11.text = Math.round(Math.random() * 1000)
	scoreText22.text = Math.round(Math.random() * 1000)
	scoreText33.text = Math.round(Math.random() * 1000)
	scoreText44.text = Math.round(Math.random() * 1000)
	scoreText55.text = Math.round(Math.random() * 1000)
	scoreText66.text = Math.round(Math.random() * 1000)
}

function actionOnClick () {
    window.location.href = "/device/allData/"+ $("#deviceSN").val() +"/view"
}

