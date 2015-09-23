package cz.jkuchar.rcba.r;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cz.jkuchar.rcba.pruning.DCBrCBA;
import cz.jkuchar.rcba.pruning.M1CBA;
import cz.jkuchar.rcba.pruning.M2CBA;
import cz.jkuchar.rcba.pruning.Pruning;
import cz.jkuchar.rcba.rules.Item;
import cz.jkuchar.rcba.rules.Rule;

@Component
@Scope("prototype")
public class RPruning {

	private List<Rule> rules;
	private List<Item> items;
	private String[] cNames;

	private Map<String, Set<String>> cache;

	@Autowired
	M2CBA m2Pruning;

	@Autowired
	M1CBA m1pruning;

	@Autowired
	DCBrCBA dcpruning;

	public RPruning() {
		this.cNames = new String[1];
		this.rules = new ArrayList<Rule>();
		this.items = new ArrayList<Item>();
		this.cache = new HashMap<String, Set<String>>();
	}

	public void setColumns(String[] cNames) {
		this.cNames = cNames;
		for (String cname : cNames) {
			this.cache.put(cname, new HashSet<String>());
		}
	}

	public void addItem(String[] values) {
		Item item = new Item();
		for (int i = 0; i < cNames.length; i++) {
			item.put(cNames[i], values[i]);
			this.cache.get(cNames[i]).add(values[i]);
		}
		this.items.add(item);
	}

	public void addRule(String rule, double confidence, double support) {
		this.rules.add(Rule.buildRule(rule, this.cache, confidence, support));
	}

	public Rule[] prune(String method) {
		Pruning pruning;
		switch (method) {
		case "dcbrcba":
			pruning = dcpruning;
			break;
		case "m1cba":
			pruning = m1pruning;
			break;
		default:
			pruning = m2Pruning;
			break;
		}
		List<Rule> results = pruning.prune(rules, items);
		return results.toArray(new Rule[results.size()]);
	}

}