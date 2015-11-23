package cn.edu.shnu.fb.interfaces.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.interfaces.assembler.ImpAssembler;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/2.
 */


@RequestMapping("/api")
@RestController
public class ImpFacade {
    @Autowired
    ImpRepository impRepository;

    @ResponseBody
    @RequestMapping(value="/i/o/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // o for oblidge
    public List<GridEntityDTO> getImpOblige(@PathVariable Integer majorId,@PathVariable Integer termCount){
        List<Imp> imps = impRepository.getObligeImpByMajorIdAndTermCount(majorId, termCount);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/e/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getImpElectable(@PathVariable Integer majorId,@PathVariable Integer termCount){
        List<Imp> imps = impRepository.getElectableImpByMajorIdAndTermCount(majorId, termCount);
        return ImpAssembler.toGridEntityDTO(imps);
    }


    @ResponseBody
    @RequestMapping(value="/i/l/{locatorId}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getImpElectableByLocator(@PathVariable Integer locatorId){
        List<Imp> imps = impRepository.getImpByLocatorId(locatorId);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/m/{majorId}/cc/{courseClassId}/ct/{courseTypeId}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getElectedImp(@PathVariable Integer majorId , @PathVariable Integer courseClassId , @PathVariable Integer courseTypeId){
        List<Imp> imps = impRepository.getElectedImp(majorId, courseClassId, courseTypeId);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/l/{locatorId}/update",method=RequestMethod.POST , consumes = "application/json")  //  l for location
    public void updateImpByLocator(@RequestBody List<GridEntityDTO> gridEntityDTOs,@PathVariable Integer locatorId){

        //add
        for(GridEntityDTO entity : gridEntityDTOs) {
            impRepository.updateImpByGridEntityAndLocatorId(entity, locatorId);
        }

        //delete
        List<Imp> currentImps = impRepository.getImpByLocatorId(locatorId);
        Iterable<Imp> differedImps ;
        if(currentImps.size() != gridEntityDTOs.size()) {
            if(gridEntityDTOs.size() != 0) {
                differedImps = ImpAssembler.differImpListWithGridEntityListByLocator(currentImps, gridEntityDTOs);
            }else{
                differedImps = currentImps;
            }
            for(Imp dImp :differedImps){
                impRepository.deleteImp(dImp);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value="/i/update/t/{termCount}",method=RequestMethod.POST , consumes = "application/json")  //  l for location
    public void updateImp(@RequestBody GridEntityDTO grid , @PathVariable Integer termCount){
         impRepository.updateImpByGridEntity(grid,termCount);
    }

    @ResponseBody
    @RequestMapping(value="/i/{impId}",method=RequestMethod.GET)
    public Imp findById(@PathVariable Integer impId){
        return impRepository.getImpById(impId);
    }

    @ResponseBody
    @RequestMapping(value="/i/comment/m/{majorId}/t/{termCount}/update",method=RequestMethod.POST , consumes = "application/json")  //  l for location
    public void updateImp(@PathVariable Integer majorId , @PathVariable Integer termCount,@RequestBody String comment){
        impRepository.persistImpComment(majorId,termCount,comment);
    }

}
