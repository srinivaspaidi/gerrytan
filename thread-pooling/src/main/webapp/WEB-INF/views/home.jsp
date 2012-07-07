<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>

<h1>Thread Pooling Demo</h1>

<p>The button below will trigger execution of a job that needs 10 seconds to complete.
Notice that when you click the response will be available immediately because we use a 
java thread pool API to run the job.</p>

<p>The thread pool is only configured to have 2 threads maximum, which means if you try to
click the button many times within short time, only 2 job at maximum will run in parallel,
the rest will be queued up until a free thread is available</p>

<p>Give it a try and inspect the server log to see how the job get executed. See 
<a href="http://wp.me/p22N42-49">my blog article</a> for more info.</p>

<form method="post">
<input type="submit" value="Run Heavy Task" />
</form>

<c:if test="${runTask}">
  <p>Heavy task #${taskId} has been submitted to the executor service. Good luck!</p>
</c:if>

</body>
</html>
