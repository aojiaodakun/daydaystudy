package com.hzk.test;


import java.util.*;
class DocumentAssociation {
    String aDocumentNo;
    String bDocumentNo;

    public DocumentAssociation(String aDocumentNo, String bDocumentNo) {
        this.aDocumentNo = aDocumentNo;
        this.bDocumentNo = bDocumentNo;
    }
}

public class TestVoucher {

    public static void main(String[] args) {
        // 创建关联关系列表
        List<DocumentAssociation> associationList = new ArrayList<>();
        associationList.add(new DocumentAssociation("A3", "BB3"));
        associationList.add(new DocumentAssociation("A4", "BB3"));
        associationList.add(new DocumentAssociation("A5", "BB3"));
        associationList.add(new DocumentAssociation("A6", "BB3"));
        associationList.add(new DocumentAssociation("A6", "BB7"));
        associationList.add(new DocumentAssociation("A6", "BB8"));
        associationList.add(new DocumentAssociation("A7", "BB8"));
        associationList.add(new DocumentAssociation("A9", "BB8"));
        associationList.add(new DocumentAssociation("A9", "BB9"));
        associationList.add(new DocumentAssociation("A1", "BB1"));
        associationList.add(new DocumentAssociation("A1", "BB2"));
        associationList.add(new DocumentAssociation("A1", "BB3"));
        associationList.add(new DocumentAssociation("A2", "BB4"));
        associationList.add(new DocumentAssociation("A2", "BB5"));
        associationList.add(new DocumentAssociation("A8", "BB10"));
        associationList.add(new DocumentAssociation("A2", "BB10"));
        associationList.add(new DocumentAssociation("A2", "A12"));

        // 指定要查询的起始单据号
        String startDocumentNo = "A2";

        // 调用BFS算法查找相关联的单据号
        Map<String, List<String>> relatedDocuments = findRelatedDocumentsBFS(startDocumentNo, associationList);

        // 打印结果
        System.out.println("与单据号 " + startDocumentNo + " 相关联的所有单据号:");
        for (String document : relatedDocuments.get("b")) {
            System.out.println(document);
        }
        System.out.println("与单据号 " + startDocumentNo + " 相关联的所有单据号:");
        for (String document : relatedDocuments.get("a")) {
            System.out.println(document);
        }
    }

    private static  Map<String, List<String>> findRelatedDocumentsBFS(String startDocumentNo, List<DocumentAssociation> associationList) {
        List<String> relatedDocuments = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> abillno = new HashSet<>();
        Set<String> bbillno = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(startDocumentNo);
        visited.add(startDocumentNo);

        while (!queue.isEmpty()) {
            String currentDocument = queue.poll();
            relatedDocuments.add(currentDocument);

            for (DocumentAssociation association : associationList) {
                if (association.aDocumentNo.equals(currentDocument) && !visited.contains(association.bDocumentNo)) {
                    queue.offer(association.bDocumentNo);
                    visited.add(association.bDocumentNo);
                    bbillno.add(association.bDocumentNo);
                }
                if (association.bDocumentNo.equals(currentDocument) && !visited.contains(association.aDocumentNo)) {
                    queue.offer(association.aDocumentNo);
                    visited.add(association.aDocumentNo);
                    abillno.add(association.aDocumentNo);
                }
            }
        }
        abillno.add(startDocumentNo);
        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("a",new ArrayList<>(abillno));
        resultMap.put("b",new ArrayList<>(bbillno));
        return resultMap;
    }
}