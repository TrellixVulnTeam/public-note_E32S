$(document).ready(function(){
	$('<a href="#top">back to top</a>').insertAfter('div.chapter p');
	$('<a id="top"></a>').prependTo('body');
	/*
	$('span.footnote')
		.insertBefore('#footer')
		.wrapAll('<ol id="notes"></ol>')
		.wrap('<li></li>');
	*/
	/*
	//��ʾ����
	var $notes = $('<ol id=notes></ol>').insertBefore('#footer');
	$('span.footnote').each(function(index){
		$('<sup>'+(index+1)+'</sup>').insertBefore(this);	//sup����this�ƶ�ǰ���õ�
		$(this).appendTo($notes).wrap('<li></li>');			//this��������sup�ģ����ֻ���ƶ�this�������ƶ�sup
	});
	*/
	/*
	//�������
	var $notes = $('<ol id=notes></ol>').insertBefore('#footer');
	$('span.footnote').each(function(index){
		$(this).before([
			'<sup>',
			(index+1),
			'</sup>'].join(''));
		.appendTo($notes)
		.wrap('<li></li>');
	});
	*/
	var $notes = $('<ol id=notes></ol>').insertBefore('#footer');
	$('span.footnote').each(function(index){
		$(this).before([
			'<a href="#footnote-',
	        index + 1,
	        '" id="context-', 
	        index + 1, 
        	'" class="context">', 
			'<sup>',
			(index+1),
			'</sup></a>'].join(''))
		.appendTo($notes)
		.append([
			'&nbsp;<a href="#context-',
			index+1,
			'">context</a>'].join(''))
		.wrap('<li id="footnote-'+(index+1)+'"></li>');
	});
});