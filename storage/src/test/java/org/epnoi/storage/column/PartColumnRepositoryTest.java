package org.epnoi.storage.column;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class PartColumnRepositoryTest extends BaseColumnRepositoryTest<PartColumn> {

    @Autowired
    PartColumnRepository repository;

    @Override
    public BaseColumnRepository<PartColumn> getRepository() {
        return repository;
    }

    @Override
    public PartColumn getEntity() {
        PartColumn column = new PartColumn();
        column.setUri("parts/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setSense("background");
        column.setContent("Miniopterus aelleni is a bat in the genus Miniopterus found in the Comoro Islands and Madagascar. It is a small, brown bat, with a forearm length of 35 to 41 mm (1.4 to 1.6 in). The long tragus (a projection in the outer ear) has a broad base and a blunt or rounded tip. The uropatagium (tail membrane) is sparsely haired. The palate is flat and there are distinct diastemata (gaps) between the upper canines and premolars. Populations of this species were previously included in Miniopterus manavi, but recent molecular studies revealed that M");
        column.setTokens("Miniopterus aelleni be a bat in the genus Miniopterus find in the Comoro Islands and Madagascar . It be a small , brown bat , with a forearm length of 35 to 41 mm ( 1.4 to 1.6 in ) . The long tragus ( a projection in the outer ear ) have a broad base and a blunt or round tip . The uropatagium ( tail membrane ) be sparsely haired . The palate be flat and there be distinct diastema ( gap ) between the upper canine and premolar . Populations of this specie be previously include in Miniopterus manavi , but recent molecular study reveal that M ");
        return column;
    }
}
