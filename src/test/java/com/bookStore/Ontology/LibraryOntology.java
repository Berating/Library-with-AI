package com.bookStore.Ontology;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.coode.owlapi.rdfxml.parser.DataQualifiedCardinalityTranslator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import com.bookStore.Entity.Book;

public class LibraryOntology {

	private OWLOntologyManager ontoManager;
	private OWLOntology bookOntology;
	private OWLDataFactory dataFactory;
	private OWLReasoner reasoner;
	
	private String ontologyIRIStr;
	private boolean contains = false;
	
	public LibraryOntology() {
		ontoManager = OWLManager.createOWLOntologyManager();
		dataFactory = ontoManager.getOWLDataFactory();
		
		loadOntologyFromFile();
		
		ontologyIRIStr = bookOntology.getOntologyID()
				.getOntologyIRI().toString() + "#";
		
	}
	
	private void loadOntologyFromFile() {
		File ontoFile = new File("src/main/java/com/bookStore/Ontology/library6.owl");
		
		try {
			bookOntology = ontoManager
					.loadOntologyFromOntologyDocument(ontoFile);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Book> getBookByGenre(String genre){
		
		ArrayList<Book> result = new ArrayList<>();
		
		OWLObjectProperty hasGenre = dataFactory
				.getOWLObjectProperty(
						IRI.create(ontologyIRIStr + "hasGenre"));
		
		OWLClass genreClass = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + genre));
		
		//Обхождаме всички аксиоми на класа
		for(OWLAxiom axiom : 
			genreClass.getReferencingAxioms(bookOntology)) {
			
			//Взимаме под внимание само аксиоми които са под клас
			if(axiom.getAxiomType() == AxiomType.SUBCLASS_OF) {

				//Взимаме всички свойства на отговарящата аксиома
				for(OWLObjectProperty op: 
					axiom.getObjectPropertiesInSignature()) {
					
					//Проверяваме ако текущото IRI е това което търсим
					if(op.getIRI().equals(hasGenre.getIRI())) {
						
						//Взимаме всички класове от аксиомата
						for(OWLClass classInAxiom: 
							axiom.getClassesInSignature()) {
							
							if(containsSuperClass(
									classInAxiom.getSuperClasses(bookOntology),
									dataFactory.getOWLClass(
											IRI.create(ontologyIRIStr + "Book")))) {
								
								contains = false;
								
								Book p = new Book();
								p.setBookName(getClassFriendlyName(classInAxiom));
								p.setIdAsString(classInAxiom.getIRI().toQuotedString());
								
								p.setGenre(getAllGenres(classInAxiom
										, hasGenre));
								
								result.add(p);							
							}
							
						}
					}
					
				}
				
			}
			
		}
		
		return result;
	}
	
	private List<String> getAllGenres(OWLClass bookClass, OWLObjectProperty hasGenre) {

		List<String> typeBook = new ArrayList<>();
		
		for(OWLAxiom axiom : 
			bookClass.getReferencingAxioms(bookOntology)) {
						
			if(axiom.getAxiomType() == AxiomType.SUBCLASS_OF) {
				
				for(OWLObjectProperty op : 
					axiom.getObjectPropertiesInSignature()) {
					
					if(op.getIRI().equals(hasGenre.getIRI())) {
						
						for(OWLClass classInAxiom: 
							axiom.getClassesInSignature()) {
							
							if(!classInAxiom.getIRI().equals(bookClass.getIRI())) {
								typeBook.add(getClassFriendlyName(classInAxiom));
							}
							
						}
					}
				}
			}			
		}
		
		
		return typeBook;
	}

	private boolean containsSuperClass(
			Set<OWLClassExpression> setOfClasses,
			OWLClass superClass) {
		
		for(OWLClassExpression clsExps : setOfClasses) {
			
			for(OWLClass cls : clsExps.getClassesInSignature()) {
				if(cls.getIRI().equals(superClass.getIRI())) {
					contains = true;
				}else {
					if(cls.getSubClasses(bookOntology).size() > 0) {
						containsSuperClass(
								cls.getSuperClasses(bookOntology), 
								superClass);
					}
				}
			}
			
		}	
		return contains;
	}
	
	private String getClassFriendlyName(OWLClass cls) {
		
		String label = cls.getIRI().toString();
		label = label.substring(label.indexOf('#') + 1);
		
		return label;	
		
	}
	
	private void saveOntology() {
		try {
			ontoManager.saveOntology(bookOntology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addBookType(String bookTypeName) {
		//Класът който ще създаваме
		OWLClass bookClass = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + bookTypeName));
		//Неговият родител (къде ще го сложим)
		OWLClass paretClass = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + "NamedBook"));
	
		//Създаваме SubClass аксиома за връзка между двата класа
		OWLSubClassOfAxiom subClassOf = dataFactory.
				getOWLSubClassOfAxiom(bookClass, paretClass);
		
		//Създаваме аксиома
		AddAxiom axiom = new AddAxiom(bookOntology, subClassOf);
		//Добавяме е към манаджера
		ontoManager.applyChange(axiom);
		//Записваме промените в онотологията
		saveOntology();		
	}
	
	public void addGenreToBook(String bookTypeName,
			String genreName) {
		//книгата към която ще слагам жанр
		OWLClass bookCls = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + bookTypeName));
		//жанр класа който ще сложа на книгата
		OWLClass genreCls = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + genreName));
		
		//Достъпваме пропертито което овръзва топигна с книгата
		OWLObjectProperty hasGenre = dataFactory.
				getOWLObjectProperty(IRI.create(
						ontologyIRIStr + "hasGenre"));
		//Правим експресион за обвързването
		OWLClassExpression clssExpression = dataFactory.
				getOWLObjectSomeValuesFrom(hasGenre, genreCls);
		
		//Правим събклас аксиом
		OWLSubClassOfAxiom axiom = dataFactory.
				getOWLSubClassOfAxiom(bookCls,clssExpression);
		
		//Създаваме аксиом за добавяне
		AddAxiom addAxiom = new AddAxiom(bookOntology, axiom);
		//Добавяме аксиома
		ontoManager.applyChange(addAxiom);
		//Записваме онтологията
		saveOntology();
		
		
	}
	
	public void renameGenre(String oldNameGenre, String newNameGenre) {
		//Взимаме инстанция на класа който ще преименуваме
		OWLClass oldClass = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + oldNameGenre));
		
		//Създаваме renamer който ще се използва за преименуване
		OWLEntityRenamer renamer = 
				new OWLEntityRenamer(ontoManager, 
						Collections.singleton(bookOntology));
		
		//Взимаме старото IRI
		IRI oldIRI = oldClass.getIRI();
		//Създаване новото IRI
		IRI newIRI = IRI.create(oldIRI.getNamespace(), newNameGenre);
		
		//Стартираме преименуванете и взимаме инстанция на всички промени
		List<OWLOntologyChange> changes = renamer.changeIRI(oldIRI, newIRI);
		//Обновяваме онтологията
		ontoManager.applyChanges(changes);
		//Записваме промените
		saveOntology();
	}
	
	public void removeBook(String name) {
		//Класът който ще изтриваме
		OWLClass bookToRemove = dataFactory.getOWLClass(
				IRI.create(ontologyIRIStr + name));
		//Създаваме remover
		OWLEntityRemover remover = 
				new OWLEntityRemover
					(ontoManager, Collections.singleton(bookOntology));
	
		//Начало на премахване
		bookToRemove.accept(remover);
		
		//Завършваме премахването
		ontoManager.applyChanges(remover.getChanges());
		
		//Записваме промените
		saveOntology();
	}
	
	
}
