from flask.ext.wtf import Form
from wtforms import StringField, SubmitField
from wtforms.validators import Required

class NameForm(Form):
	name = StringField('Whats your name', validators=[Required()]);
	submit = SubmitField('Submit');