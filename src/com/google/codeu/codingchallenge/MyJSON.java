// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

final class MyJSON implements JSON {
  private Map<String ,String> keyValue;//Key-Value Pair
  private Map<String ,String> keyObject;//Key-Object Pair
  private  String originalText;//Get the Original JSON Text
  private MyJSONParser parser = new MyJSONParser();
  public MyJSON(Map<String,String> keyValue, Map<String,String> keyObject, String originalText)
  {
    this.keyValue = keyValue;
    this.keyObject = keyObject;
    this.originalText = originalText;
  }
  public MyJSON()
  {
    this.keyValue = new HashMap<>();
    this.keyObject = new HashMap<>();
  }
  @Override
  public JSON getObject(String name) {
    if (keyObject.containsKey(name))
    {
      String result = keyObject.get(name);
      try
      {
        return parser.parse(result);
      }
      catch (IOException e)
      {
        return  null;
      }
    }
    return null;
  }

  @Override
  public JSON setObject(String name, JSON value) {
    MyJSON newJSON = (MyJSON)value;
    keyObject.put(name,newJSON.originalText);//Before parse, all objects are still represented as string
    return this;
  }

  @Override
  public String getString(String name)
  {
    String result = keyValue.get(name);
    return result;
  }

  @Override
  public JSON setString(String name, String value){
    keyValue.put(name,value);
    return this;
  }

  @Override
  public void getObjects(Collection<String> names) {
    names = keyObject.keySet();
  }

  @Override
  public void getStrings(Collection<String> names) {
    names = keyValue.keySet();
  }
}
