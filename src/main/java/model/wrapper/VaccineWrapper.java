package model.wrapper;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.vaccine.VaccineEntity;

/**
 * Вспомогательный класс для обёртывания списка адресатов.
 * Используется для сохранения списка адресатов в XML.
 * 
 * @author Marco Jakob
 */
@XmlRootElement(name = "vaccines")
public class VaccineWrapper {
	List<VaccineEntity> vaccines = new ArrayList<>();



    @XmlElement(name = "vaccine")
    public List<VaccineEntity> getListVaccines() {
        return vaccines;
    }

    public void setListVaccines(List<VaccineEntity> vaccines) {
        this.vaccines = vaccines;
    }
}

