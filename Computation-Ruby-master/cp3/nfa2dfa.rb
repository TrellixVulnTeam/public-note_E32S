#�������ƶ��Ĳ�ȷ���������Զ���

require 'set'

class FARule < Struct.new(:state, :character, :next_state)
	def applies_to?(state, character)	#�ж�״̬��������ַ� �Ƿ�͸ù���ƥ��
		self.state == state && self.character == character
	end
	
	def follow				#���ظ�rule��Ӧ�����״̬
		self.next_state
	end
	
	def inspect
		"#<FARule #{state.inspect} --#{character} --> #{next_state.inspect}>"
	end
end

class NFARulebook < Struct.new(:rules)
	def next_states(states, character)	#��ǰ״̬�����е�ÿ��״̬ ����������һ��״̬ ������ϳ�״̬����
		states.flat_map { |state| follow_rules_for(state, character) }.to_set	#ʹ��flat_map��Ϊ�˽����ɵĽ��ȫ���������ʽ����.���򽫻��Ǹ���ά����
	end
	
	def follow_free_moves(states)
		more_states = next_states(states, nil)
		
		if more_states.subset?(states)	#��states����ת�ƺ�õ���״̬����ת��֮ǰ��ȡ���ת�ƺ��״̬����ת��ǰ���Ӽ���˵���Ѿ�����ת�������.(����1->2,4  1,2,4->2,4����Ϊ2��4û�취ת��)
			states
		else
			follow_free_moves(states + more_states)	#����ת��
		end
	end
	
	def follow_rules_for(state, character)		#
		rules_for(state, character).map(){ |rule| rule.follow }
	end
	
	def rules_for(state, character)		#��һ��״̬����һ�������£��ҵ���Ӧ�����й��򣬲����һ������
		rules.select{ |rule| rule.applies_to?(state, character) }	#ѡ������������ļ���
	end
end

class NFA < Struct.new(:current_states, :accept_states, :rulebook)
	def accepting?
		(current_states & accept_states).any?		#��#���ҵ�ǰ״̬����ɽ���״̬���Ľ�������any?�ж��Ƿ����.
	end
	
	def read_character(character)	#��һ���ַ���������״̬ת��
		self.current_states = rulebook.next_states(rulebook.follow_free_moves(current_states), character)	#�ڽ����´��ж�ǰ������һ�������ƶ�
		self.current_states = rulebook.follow_free_moves(current_states)									#�������Ӧ����һ�������ƶ�
	end
	
	def read_string(string)	#�����ַ���������ÿ���ַ�������
		string.chars.each { |character| read_character(character) }	#string.chars �õ��ַ�����Ӧ���ַ�����
	end
end

class NFADesign < Struct.new(:start_state, :accept_states, :rulebook)
	def accepts?(string)
		nfa = NFA.new(Set[start_state], accept_states, rulebook)
		nfa.read_string(string)
		nfa.accepting?
	end
end

#ȷ���������Զ���
class DFARulebook < Struct.new(:rules)
	def next_state(state, character)		#����״̬�������ַ����ҵ���ƥ�����һ��״̬
		rule_for(state, character).follow
	end
	
	def rule_for(state, character)			#�����еĹ������ҵ�ƥ��Ĺ���
		rules.detect{ |rule| rule.applies_to?(state, character) }
	end
end

class DFA < Struct.new(:current_state, :accept_states, :rulebook)
	def accepting?		#�жϵ�ǰ��״̬�Ƿ�Ϊ����״̬
		accept_states.include?(current_state)
	end
	
	def read_character(character)	#��һ���ַ���������״̬ת��
		self.current_state = rulebook.next_state(current_state, character)
	end
	
	def read_string(string)	#�����ַ���������ÿ���ַ�������
		string.chars.each { |character| read_character(character) }	#string.chars �õ��ַ�����Ӧ���ַ�����
	end
end

class DFADesign < Struct.new(:start_state, :accept_states, :rulebook)
	def to_dfa
		DFA.new(start_state, accept_states, rulebook)
	end
	
	def accepts?(string)
		to_dfa.tap { |dfa| dfa.read_string(string) }.accepting?		#tap, �����󴫵����������ֵ���������������
	end
end

#NFASimulation