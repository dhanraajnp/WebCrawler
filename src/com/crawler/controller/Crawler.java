package com.crawler.controller;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Scanner;

public class Crawler {

    private HashSet<String> links;
    private static int counter = 0; 
    private static boolean limitCraw = false;

    public Crawler() {
        links = new HashSet<String>();
    }
   
    public static void main(String[] args) {

    	Scanner sc = new Scanner(System.in);
    	System.out.println("Do you want to crawl for the given number of links or without the limit ? select from below options,");
    	System.out.println("1. Limit the crawl link count.");
    	System.out.println("2. Crawl for UNLIMITED number of times.");
    	
    	int temp = sc.nextInt();
    	if(temp == 1){
    		limitCraw = true; 
    		System.out.println("How many links do you want to crawl it for ?");
        	int count = sc.nextInt();        	
        	counter = count;
    	}else{
    		limitCraw = false;
    	}
    	//Pick a URL and call for the first time.
    	new Crawler().getPageLinks("http://redbus.in");
    	
    }

	private void getPageLinks(String URL) {
        
		jdbcConnection connect = new jdbcConnection(); 
		if( limitCraw && counter <= 0){
			System.out.println("Exiting since the count for max number of links  to be crawled has been reached!"+links.size());
			System.exit(1); //Using it only here, cannot use it while deploying in server. 
		}
		
    	//Format the URL to remove duplicates.
    	if(URL.endsWith("/")){
    		URL = URL.substring(0, URL.length()-1);
    	}
    	
    	//Check if you have already crawled the URL
    	//Purposely checking for duplicate element, we can know this while adding too.
    	if (!links.contains(URL) && !URL.equalsIgnoreCase("")){		//Using hash set to avoid the duplicate insertion of the URLs. 
            try {
                //If not, add it to the index
                links.add(URL);
                connect.saveUrl(URL);	//Saving the URL in DB.
                System.out.println(URL);
                --counter;               
                
                //Fetch the HTML code
                Document document = Jsoup.connect(URL).get();             
                
                //Parse the HTML to extract links(href) to other URLs
                Elements linksOnPage = document.select("a[href]");
                	//System.out.println("size():"+linksOnPage.size());
                
                //For each extracted URL... repeat the process.
                for (Element page : linksOnPage) {             	
                    getPageLinks(page.attr("abs:href"));
                }
                
            }catch(IllegalArgumentException ex){
            	System.err.println("For the URL : '" + URL + "': " + ex.getMessage());
            	return;
            }catch(HttpStatusException hEx){
            	System.out.println("HTTP : 404, Page not Found!!");
            	System.out.println(hEx);
            }catch(UnknownHostException unEx){
            	System.out.println("Please check the internet connection!");
            }catch (IOException e) {          
                System.err.println("For '" + URL + "': " + e.getMessage());
                System.out.println(e);
            }
        }
    	
    }

}