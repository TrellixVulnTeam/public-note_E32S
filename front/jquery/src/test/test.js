$(document).ready(function(){
	$('body').click(function(event){
		alert(2);
	});
	
	$('input').click(function(event){
		alert(1);
		//event.stopPropagation();	//��ֹ���¼����������ᵯ��alert(2)�� ���ǲ�������ֹĬ�ϲ������ᷢ��ҳ����ת��
		//return false;				//��ֹ���¼�������Ҳ��ֹ��Ĭ�ϲ�����
	});
});

function onsubmitAction(){
	alert(3);
	return true;
}