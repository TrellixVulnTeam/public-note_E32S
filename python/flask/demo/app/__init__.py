from flask import Flask, render_template
from config import config

def create_app(config_name):
	app = Flask(__name__);
	app.config.from_object(config[config_name]);
	config[config_name].init_app(app);
	return app;