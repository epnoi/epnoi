package org.epnoi.model;

import java.util.ArrayList;
import java.util.Iterator;

public class User implements Resource {

	private Long id;
	private String uri;
	private String resource;
	private String description;
	private String name;
	private String password;

	private ArrayList<Tagging> tagApplied;
	private ArrayList<String> friends;
	private ArrayList<String> favouritedWorkflows;
	private ArrayList<String> favouritedFiles;
	private ArrayList<String> groups;
	private ArrayList<String> files;
	private ArrayList<String> workflows;
	private ArrayList<String> packs;
	private ArrayList<Action> actions;
	private ArrayList<String> searchs;
	private ArrayList<String> informationSourceSubscriptions;
	private ArrayList<String> knowledgeObjects;
	

	public User() {
		this.tagApplied = new ArrayList<Tagging>();
		this.favouritedWorkflows = new ArrayList<String>();
		this.favouritedFiles = new ArrayList<String>();
		this.groups = new ArrayList<String>();
		this.files = new ArrayList<String>();
		this.workflows = new ArrayList<String>();
		this.friends = new ArrayList<String>();
		this.packs = new ArrayList<String>();
		this.actions = new ArrayList<Action>();
		this.searchs = new ArrayList<String>();
		this.informationSourceSubscriptions = new ArrayList<String>();
		this.knowledgeObjects = new ArrayList<String>();
	}

	//---------------------------------------------------------------------------------
	
	public String getPassword() {
		return password;
	}
	
	//---------------------------------------------------------------------------------

	public void setPassword(String password) {
		this.password = password;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getFavouritedFiles() {
		return favouritedFiles;
	}
	
	//---------------------------------------------------------------------------------

	public void setFavouritedFiles(ArrayList<String> favouritedFiles) {
		this.favouritedFiles = favouritedFiles;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getWorkflows() {
		return workflows;
	}
	
	//---------------------------------------------------------------------------------

	public void setWorkflows(ArrayList<String> workflows) {
		this.workflows = workflows;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getFriends() {
		return friends;
	}
	
	//---------------------------------------------------------------------------------

	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}
	
	//---------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}
	
	//---------------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getFavouritedWorkflows() {
		return favouritedWorkflows;
	}
	
	//---------------------------------------------------------------------------------

	public void setFavouritedWorkflows(ArrayList<String> favouritedWorkflows) {
		this.favouritedWorkflows = favouritedWorkflows;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getGroups() {
		return groups;
	}
	
	//---------------------------------------------------------------------------------

	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}
	
	//---------------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}
	
	//---------------------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}
	
	//---------------------------------------------------------------------------------

	public String getResource() {
		return resource;
	}
	
	//---------------------------------------------------------------------------------

	public void setResource(String resource) {
		this.resource = resource;
	}

	//---------------------------------------------------------------------------------
	
	public ArrayList<Tagging> getTagApplied() {
		return tagApplied;
	}
	
	//---------------------------------------------------------------------------------

	public void setTagApplied(ArrayList<Tagging> tagApplied) {
		this.tagApplied = tagApplied;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getFiles() {
		return files;
	}
	
	//---------------------------------------------------------------------------------

	public void setFiles(ArrayList<String> files) {
		this.files = files;
	}
	
	//---------------------------------------------------------------------------------

	public Long getId() {
		return id;
	}
	
	//---------------------------------------------------------------------------------

	public void setId(Long iD) {
		id = id;
	}
	
	//---------------------------------------------------------------------------------

	public String getName() {
		return name;
	}
	
	//---------------------------------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}
	
	//---------------------------------------------------------------------------------

	public void addTagging(String tag) {
		// System.out.println(">> entra " + tag);
		Iterator<Tagging> taggingIterator = this.tagApplied.iterator();
		boolean finded = false;
		while (!finded && taggingIterator.hasNext()) {
			Tagging tagging = taggingIterator.next();
			finded = tagging.getTag().equals(tag);
			if (finded) {

				tagging.setNumberOfTaggings(tagging.getNumberOfTaggings() + 1);
			}
		}
		if (!finded) {
			// System.out.println(">> creo " + tag + " en " + this.ID);
			Tagging newTagging = new Tagging();
			newTagging.setTag(tag);
			newTagging.setNumberOfTaggings(1);
			this.tagApplied.add(newTagging);

		}
	}

	//---------------------------------------------------------------------------------
	
	public ArrayList<String> getPacks() {
		return packs;
	}

	//---------------------------------------------------------------------------------
	
	public void setPacks(ArrayList<String> packs) {
		this.packs = packs;
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<String> getSearchs() {
		return searchs;
	}
	
	//---------------------------------------------------------------------------------

	public void setSearchs(ArrayList<String> searchs) {
		this.searchs = searchs;
	}
	
	//---------------------------------------------------------------------------------

	public void addSearch(String search) {
		this.searchs.add(search);
	}
	
	//---------------------------------------------------------------------------------

	public ArrayList<Action> getActions() {
		return actions;
	}
	
	//---------------------------------------------------------------------------------

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	//---------------------------------------------------------------------------------
	
	public ArrayList<String> getInformationSourceSubscriptions() {
		return informationSourceSubscriptions;
	}
	
	//---------------------------------------------------------------------------------

	public void setInformationSourceSubscriptions(
			ArrayList<String> informationSourceSubscriptions) {
		this.informationSourceSubscriptions = informationSourceSubscriptions;
	}
	
	//---------------------------------------------------------------------------------

	public void addInformationSourceSubscription(String informationSourceSubscription){
		this.informationSourceSubscriptions.add(informationSourceSubscription);
	}

	//---------------------------------------------------------------------------------
	
	public void addKnowledgeObject(String knowledgeObject){
		this.knowledgeObjects.add(knowledgeObject);
	}
	
	//---------------------------------------------------------------------------------
	
	
	public ArrayList<String> getKnowledgeObjects() {
		return knowledgeObjects;
	}
	
	//---------------------------------------------------------------------------------

	public void setKnowledgeObjects(ArrayList<String> knowledgeObjects) {
		this.knowledgeObjects = knowledgeObjects;
	}
	
	//---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "User [ID=" + id + ", URI=" + uri + ", resource=" + resource
				+ ", description=" + description + ", name=" + name
				+ ", password=" + password + ", tagApplied=" + tagApplied
				+ ", friends=" + friends + ", favouritedWorkflows="
				+ favouritedWorkflows + ", favouritedFiles=" + favouritedFiles
				+ ", groups=" + groups + ", files=" + files + ", workflows="
				+ workflows + ", packs=" + packs + ", actions=" + actions
				+ ", searchs=" + searchs + ", informationSourceSubscriptions="
				+ informationSourceSubscriptions + ", knowledgeObjects="
				+ knowledgeObjects + "]";
	}
	
	//---------------------------------------------------------------------------------

}
